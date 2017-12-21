package com.genfare.farebox.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;


public class FareBox{
	

	private static final Logger log = Logger.getLogger(FareBox.class.getName());

	public static final String AUTH_HEADER_PROPERTY = "Authorization";
	public static final String AUTH_HEADER_PREFIX = "Basic";
	
	static DeviceAuthResponse deviceAuthResponse = null;
	static Map<String, String> headers = new LinkedHashMap<String, String>();
	static String response = null;
	static Properties prop = new Properties();

	//public void execute(JobExecutionContext context) throws JobExecutionException {

	public static void main(String[] args) {
		String authenticationUrl = null;
		String fareBoxSerialNumber = null;
		String fareBoxPassword = null;
		String tenant = null;
		String deviceType = null;
		InputStream input = null;
		String environment = null;
		try {
			String filename = "device.properties";
			input = FareBox.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				log.info("Sorry, unable to find " + filename);

			}
			prop.load(input);

			authenticationUrl = prop.getProperty("authenticationUrl");
			fareBoxSerialNumber = prop.getProperty("fareBoxSerialNumber");
			fareBoxPassword = prop.getProperty("fareBoxPassword");
			tenant = prop.getProperty("tenant");
			deviceType = prop.getProperty("deviceType");
			environment = prop.getProperty("environment");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		deviceAuthResponse = authenticate(authenticationUrl, fareBoxSerialNumber, fareBoxPassword, tenant, deviceType,environment);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static DeviceAuthResponse authenticate(String authenticationUrls, String fareBoxSerialNumber, 
			String fareBoxPassword,String tenant,String  deviceType,String environment) {
		String authUrlString = "https://";
		environment=environment.trim();
		if(environment.equals("local"))
		{
			authUrlString = "http://";
		}
		DeviceAuthResponse deviceAuthResponse = null;

		authUrlString = authUrlString+authenticationUrls + "?tenant=" + tenant + "&type=" + deviceType;
		byte[] authorizationBytes = (fareBoxSerialNumber + ":" + fareBoxPassword).getBytes();
		String authorizationHeader = AUTH_HEADER_PREFIX + " "
				+ new String(org.apache.commons.codec.binary.Base64.encodeBase64(authorizationBytes));
		log.info(authorizationHeader);

		headers.put(AUTH_HEADER_PROPERTY, authorizationHeader);
		headers.put("Content-Type", "application/json");
		try {
				MultivaluedMap head = new MultivaluedHashMap();
				
				for (Map.Entry<String, String> header : headers.entrySet()) {
						head.add(header.getKey(), header.getValue());
					}
				
				   javax.ws.rs.client.Client client = ClientBuilder.newClient(); 
				   Response response = client.target(authUrlString).request().headers(head).post(null);
				   
				   if(response.getStatus()==200)
				   {
				   String responseAsString = response.readEntity(String.class);
				   ObjectMapper mapper2 = new ObjectMapper();
				   deviceAuthResponse = mapper2.readValue(responseAsString, DeviceAuthResponse.class);
				   log.info("Successfully authenticated...");
				}
				
			} catch (Exception e) {
				log.log(Level.WARNING, "Error posting auth", e);
			}
			
		return deviceAuthResponse;
	}
}

