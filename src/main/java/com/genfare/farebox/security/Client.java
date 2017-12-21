package com.genfare.farebox.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genfare.farebox.main.DeviceAuthResponse;
import com.genfare.farebox.remote.RemoteSystemException;
import com.genfare.farebox.remote.RemoteValidationException;

public class Client {

	private static final Logger log = Logger.getLogger(Client.class.getName());
	private static final boolean DISPLAY_LOGS = false; 
	
	public DeviceAuthResponse auth(String urlStr, Map<String, String> headers) throws RemoteSystemException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DeviceAuthResponse resp = null;

        String json = null;

        json = postTrySsl(urlStr, headers, null);

        if(json != null) {
            
            try {
                resp = mapper.readValue(json, DeviceAuthResponse.class);
            } catch (JsonParseException e) {
                log.warning("Error parsing device response: " + e.getMessage());
                e.printStackTrace();
            } catch (JsonMappingException e) {
                log.warning("Error mapping device response: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                log.warning("Error reading device response: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        return resp;
    }
    
    public String postTrySsl(String url, Map<String, String> headers, String body) throws RemoteSystemException {

        String response = null;
        
    	boolean success = false;
    	
    	String sslUrl = "http://" + url;
    
    	try {
    		log.log(Level.INFO, "Trying NON SSL URL");
    		response = post(sslUrl, headers, body);
    		success = true;
    	} catch (RemoteValidationException rve) {
            log.log(Level.WARNING, "SSL connection failed", rve);
            rve.printStackTrace();
        } catch (RemoteSystemException e) {
    		log.log(Level.WARNING, "SSL connection failed", e);
    		if((e.getResponseCode() == 403) || (e.getResponseCode() == 401)) {
    		    throw e;
    		}
    	}
    	
    	if(!success) {
    		log.log(Level.INFO, "Falling back to unsecure connection");
    		
    		String noSslUrl = "https://" + url;
    		
        	try {
        		response = post(noSslUrl, headers, body);
        		success = true;
        	} catch (RemoteValidationException rve) {
                log.log(Level.WARNING, "Unsecure connection failed", rve);
                rve.printStackTrace();
            } catch (RemoteSystemException e) {
        		log.log(Level.WARNING, "Unsecure connection failed", e);
                throw e;
        	}
    	}
    	
    	if(!success) {
    		throw new RemoteSystemException("Error connecting to server");
    	}
        
        return response;
    
    }
 

    public String post(String urlStr, Map<String, String> headers, String body) throws RemoteSystemException, RemoteValidationException {

        String response = null;
        
        try {
	        URL url = new URL(urlStr);
	        if(DISPLAY_LOGS) {
	            log.info("POST body: " + body);
	        }
	        
	
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        
	        if(headers != null) {
	        	
	            for (Map.Entry<String,String> header : headers.entrySet()) {
	            	conn.setRequestProperty(header.getKey(), header.getValue());
	            }
	        	
	        }

	        byte[] postDataBytes = null;
	        int contentLength = 0;
	        
	        if(body != null) {
	        	postDataBytes = body.getBytes("UTF-8");
	        	contentLength = postDataBytes.length;
	        }

	        conn.setRequestProperty("Content-Length", String.valueOf(contentLength));
	        conn.setDoOutput(true);
	        conn.setConnectTimeout(20 * 1000);
	        conn.setReadTimeout(60 * 1000);
	        conn.connect();
	        
	        if(postDataBytes != null) {
	        	conn.getOutputStream().write(postDataBytes);
	        }
	        
	        log.info("Outputstream written");
	        
	        int responseCode = conn.getResponseCode();
	        String responseMessage = conn.getResponseMessage();
	        
	        log.info("Response Code: " + responseCode + ", Message: " + responseMessage);
	        
	        if(responseCode == 200) {
		        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	                
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
	                
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                reader.close();
                response = stringBuilder.toString();
                System.out.println("Response: "+response);
                if(response == null) {
                    if(responseMessage.equals("OK")) {
                        log.info("Response was identified as 200 OK.");
                        return "true";
                    } else {
                        log.info("Response was identified as no body, not 200 OK, error.");
                        return null;
                    }
		        } else {
		            if(DISPLAY_LOGS) {
		                log.info("Response was identified as: "+response);
		            } else {
		                log.info("Response was identified as a correct response.");
		            }
		            return response;
		        }
	        } else if (422 == responseCode) {
	            
	            log.log(Level.WARNING, "Error posting - Response Code: " + responseCode);
	            
	            if (null != conn.getErrorStream()) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
                    
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;
                    
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    
                    reader.close();
                    
                    String errorResponse = stringBuilder.toString();
                    
                    log.log(Level.WARNING, "Error posting - Error Response: " + errorResponse);
                }
	            
                RemoteValidationException rve = new RemoteValidationException("Error posting - Response Code: " + responseCode);
                rve.setResponseCode(responseCode);
                throw rve;
	            
	        } else {
	        	log.log(Level.WARNING, "Error posting - Response Code: " + responseCode);
	        	RemoteSystemException rse = new RemoteSystemException("Error posting - Response Code: " + responseCode);
	        	rse.setResponseCode(responseCode);
	        	throw rse;
	        }
        } catch(IOException e) {
        	log.log(Level.WARNING, "Error posting", e);
	    	throw new RemoteSystemException(e);
	    }
    }
}