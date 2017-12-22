package com.genfare.farebox.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genfare.farebox.main.DeviceAuthResponse;

public class Sample {
	private static final Logger log = Logger.getLogger(Sample.class.getName());

	public DeviceAuthResponse authenticate(String authUrlString, Map<String, String> headers) {
		DeviceAuthResponse deviceAuthResponse = null;
		
		
		HttpURLConnection conn = null;
		String sslUrl = "http://" + authUrlString;
		
	//---------------------------------------------------------------	
		
		
		MultivaluedMap<String, Object> head = new MultivaluedHashMap<String,Object>(); 
		 for (Map.Entry<String, String> header : headers.entrySet()) {
				head.add(header.getKey(), header.getValue());
			}
		javax.ws.rs.client.Client client = ClientBuilder.newClient(); 
		   Response response2 = client.target(sslUrl).request().headers(head).post(null);

		   String responseAsString = response2.readEntity(String.class);
		   ObjectMapper mapper2 = new ObjectMapper();
			try {
				deviceAuthResponse = mapper2.readValue(responseAsString, DeviceAuthResponse.class);
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
        
		
	
		
	//----------------------------------------------------------------	
		try {
			URL url = new URL(sslUrl);
			try {
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				if (headers != null) {

					for (Map.Entry<String, String> header : headers.entrySet()) {
						conn.setRequestProperty(header.getKey(), header.getValue());
					}

				}
				conn.setConnectTimeout(60 * 1000);
				conn.setReadTimeout(60 * 1000);
				conn.connect();
				int code = conn.getResponseCode();
				if (code == 200) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

					StringBuilder stringBuilder = new StringBuilder();
					String line = null;

					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line + "\n");
					}
					reader.close();
					String response;
					response = stringBuilder.toString();
					log.info(response);
					ObjectMapper mapper = new ObjectMapper();
					deviceAuthResponse = mapper.readValue(response, DeviceAuthResponse.class);
					return deviceAuthResponse;
				}
			} catch (IOException e) {
				e.getMessage();
 				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return deviceAuthResponse;
	}

}
