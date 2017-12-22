package com.genfare.farebox.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genfare.farebox.main.AwsResponse.AwsCredentials;
import org.apache.commons.codec.binary.Base64;


public class FareBox {

	private static final Logger log = Logger.getLogger(FareBox.class.getName());

	public static final String AUTH_HEADER_PROPERTY = "Authorization";
	public static final String AUTH_HEADER_PREFIX = "Basic";

	static DeviceAuthResponse deviceAuthResponse = null;
	static String response = null;
	static Properties prop = new Properties();

	// public void execute(JobExecutionContext context) throws JobExecutionException
	// {

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

		deviceAuthResponse = authenticate(authenticationUrl, fareBoxSerialNumber, fareBoxPassword, tenant, deviceType,
				environment);
	}

	private static DeviceAuthResponse authenticate(String authenticationUrl, String fareBoxSerialNumber,
			String fareBoxPassword, String tenant, String deviceType, String environment) {
		String authUrlString = "https://";
		environment = environment.trim();
		if (environment.equals("local")) {
			authUrlString = "http://";
		}
		DeviceAuthResponse deviceAuthResponse = null;

		authUrlString = authUrlString + authenticationUrl + "?tenant=" + tenant + "&type=" + deviceType;
		byte[] authorizationBytes = (fareBoxSerialNumber + ":" + fareBoxPassword).getBytes();
		
		String authorizationHeader = AUTH_HEADER_PREFIX + " "
				+ new String(Base64.encodeBase64(authorizationBytes));

		log.info(authorizationHeader);
		javax.ws.rs.client.Client client = ClientBuilder.newClient();
		try {
			MultivaluedMap<String, Object> head = new MultivaluedHashMap<String, Object>();
			head.add(AUTH_HEADER_PROPERTY, authorizationHeader);
			head.add("Content-Type", "application/json");

			Response response = client.target(authUrlString).request().headers(head).post(null);
			
			String responseAsString = response.readEntity(String.class);
		
			if (response.getStatus() != 200 && response.getStatus() != 201) {
				log.info("Failed : HTTP error code : " + response.getStatus() + responseAsString);
			} else {
				ObjectMapper mapper2 = new ObjectMapper();
				deviceAuthResponse = mapper2.readValue(responseAsString, DeviceAuthResponse.class);
				encodeAWSCredentials(deviceAuthResponse);
				log.info("Successfully authenticated...");
			}
		}

		catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return deviceAuthResponse;
	}

	private static void encodeAWSCredentials(DeviceAuthResponse deviceAuthResponse) {
		AwsCredentials awsCredentials = deviceAuthResponse.getAws().getCredentials();
		String accessKey = awsCredentials.getAccessKey();
		String secretKey = awsCredentials.getSecretKey();
		String sessionId = awsCredentials.getSessionId();
		byte[] authorizationBytes = (accessKey + " | " + secretKey +" | "+sessionId).getBytes();
		String awsAuthorizationKey = new String(Base64.encodeBase64(authorizationBytes));
		MultivaluedMap<String, Object> head = new MultivaluedHashMap<String, Object>();
		head.add(AUTH_HEADER_PROPERTY, awsAuthorizationKey);
		String uploadUrlString = prop.getProperty("uploadUrlString");
		javax.ws.rs.client.Client client = ClientBuilder.newClient();
		WebTarget target = client.target(uploadUrlString);
		Response response = target.request().headers(head).post(Entity.entity(awsCredentials,"application/xml"));
	
		
	}
}
