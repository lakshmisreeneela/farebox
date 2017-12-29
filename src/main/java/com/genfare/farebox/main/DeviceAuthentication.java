package com.genfare.farebox.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genfare.farebox.response.DeviceAuthResponse;

public class DeviceAuthentication {

	private static final Logger log = Logger.getLogger(FareBox.class.getName());

	public static final String AUTH_HEADER_PROPERTY = "Authorization";
	public static final String AUTH_HEADER_PREFIX = "Basic";

	static DeviceAuthResponse deviceAuthResponse = null;
	static String response = null;
	static Properties prop = new Properties();

	String authenticationUrl = null;
	String fareBoxSerialNumber = null;
	String fareBoxPassword = null;
	String tenant = null;
	String deviceType = null;
	InputStream input = null;
	String environment = null;

	public DeviceAuthResponse authenticate() {
		try {
			String filename = "device.properties";
			input = FareBox.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				log.info("Sorry, unable to find " + filename);

			}
			prop.load(input);

			authenticationUrl = prop.getProperty("authenticationUrl");
			//authenticationUrl="cdta-intg.gfcp.io/services/device/v5/auth";
			fareBoxSerialNumber = prop.getProperty("fareBoxSerialNumber");
			fareBoxPassword = prop.getProperty("fareBoxPassword");
			tenant = prop.getProperty("tenant");
			deviceType = prop.getProperty("deviceType");
			environment = prop.getProperty("environment");
			//environment="local";
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		String authUrlString = "https://";
		environment = environment.trim();
		//if (environment.equals("local")) {
		//authUrlString = "http://";
		//}
		DeviceAuthResponse deviceAuthResponse = null;

		authUrlString = authUrlString + authenticationUrl + "?tenant=" + tenant + "&type=" + deviceType;
		byte[] authorizationBytes = (fareBoxSerialNumber + ":" + fareBoxPassword).getBytes();

		String authorizationHeader = AUTH_HEADER_PREFIX + " " + new String(Base64.encodeBase64(authorizationBytes));

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
				//encodeAWSCredentials(deviceAuthResponse);
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

	
}
