package com.genfare.farebox.clientrequest;

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
import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.farebox.main.EnvironmentSetting;
import com.genfare.farebox.util.PropertiesRetrieve;

public class DeviceAuthentication {

	private static final Logger log = Logger.getLogger(DeviceAuthentication.class.getName());

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

	public DeviceAuthResponse authenticate(String fareBoxSerialNumber,String fareBoxPassword) {
		
		try {
			String filename = "device.properties";
			input = DeviceAuthentication.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				log.info("Sorry, unable to find " + filename);

			}
			prop.load(input);
			
			
			PropertiesRetrieve propertiesRetrieve = new PropertiesRetrieve();
			Properties prop = propertiesRetrieve.getProperties();
			tenant = prop.getProperty("tenant");
			deviceType = prop.getProperty("deviceType");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		DeviceAuthResponse deviceAuthResponse = null;

		
		
		
		
		String authUrlString = "https://" +EnvironmentSetting.getEnvironment()+"/services/device/v5/auth" + "?tenant=" + EnvironmentSetting.getTenant() + "&type=" + deviceType;
		byte[] authorizationBytes = (fareBoxSerialNumber + ":" + fareBoxPassword).getBytes();

		String authorizationHeader = AUTH_HEADER_PREFIX + " " + new String(Base64.encodeBase64(authorizationBytes));

		System.out.println(authorizationHeader);
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
				EnvironmentSetting.setFbSerialNumber(fareBoxSerialNumber);
				EnvironmentSetting.setFbPassword(fareBoxPassword);
				
				
				ObjectMapper mapper2 = new ObjectMapper();
				deviceAuthResponse = mapper2.readValue(responseAsString, DeviceAuthResponse.class);
				System.out.println("Successfully authenticated...");
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
