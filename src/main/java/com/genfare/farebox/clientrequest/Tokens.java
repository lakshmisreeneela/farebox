package com.genfare.farebox.clientrequest;

import java.util.Properties;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;

import com.amazonaws.util.json.JSONObject;
import com.genfare.farebox.util.PropertiesRetrieve;
import javax.ws.rs.client.Client;
public class Tokens {
	JSONObject json =null;

	public static final String AUTH_HEADER_PROPERTY = "Authorization";
	public JSONObject getToken()
	{
	PropertiesRetrieve propertiesRetrieve = new PropertiesRetrieve();
	Properties prop = propertiesRetrieve.getProperties();
	String auth_username = prop.getProperty("auth_username_i");
	String auth_password = prop.getProperty("auth_password_i");
	byte[] authorizationBytes = (auth_username + ":" +auth_password ).getBytes();
	String authorizationHeader = "Bearer" + " " + new String(Base64.encodeBase64(authorizationBytes));
	String authUrlString ="https://cdta-intg.gfcp.io/authenticate/oauth/token?grant_type=client_credentials&client_id=coocoo";
	Client client = ClientBuilder.newClient();
	try {
		MultivaluedMap<String, Object> head = new MultivaluedHashMap<String, Object>();
		head.add(AUTH_HEADER_PROPERTY,authorizationHeader );
		head.add("Content-Type", "application/json");

		Response response = client.target(authUrlString).request().headers(head).post(null);
		if(response.getStatus() ==200)
		{
		String responseAsString = response.readEntity(String.class);
		json = new JSONObject(responseAsString);
		}
	}
		catch(Exception ex)
	{
			ex.printStackTrace();
	}
	return json;
}
}
