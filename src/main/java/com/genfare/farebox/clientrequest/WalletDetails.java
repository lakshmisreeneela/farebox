package com.genfare.farebox.clientrequest;

import java.util.Properties;
import java.util.Scanner;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.genfare.farebox.util.PropertiesRetrieve;

import javax.ws.rs.client.Client;

public class WalletDetails {
	
	public static final String AUTH_HEADER_PROPERTY = "Authorization";
	public static final String AUTH_HEADER_PREFIX = "Basic";
	
	
	public static JSONObject userLogin(String username)
	{
		JSONObject json = null;
		PropertiesRetrieve PropertiesRetrieve = new PropertiesRetrieve();
		Properties prop = PropertiesRetrieve.getProperties();
		String auth_username = prop.getProperty("auth_username");
		String auth_password = prop.getProperty("auth_password");
		
		byte[] authorizationBytes = (auth_username + ":" +auth_password ).getBytes();
		String authorizationHeader = AUTH_HEADER_PREFIX + " " + new String(Base64.encodeBase64(authorizationBytes));
		
		MultivaluedMap<String, Object> head = new MultivaluedHashMap<String, Object>();
		head.add(AUTH_HEADER_PROPERTY, authorizationHeader);
		
		Scanner sc = new Scanner(System.in);
		System.out.println("please enter Password");
		String password = sc.nextLine();
		sc.close();
		
		String uploadURL = "https://cdta-dev3.gfcp.io/authenticate/oauth/token?grant_type=password&client_id=coocoo";
		uploadURL = uploadURL+"&"+"username="+username+"&"+"password="+password;
		
		Client client = ClientBuilder.newClient();
		Response response = client.target(uploadURL).request().headers(head).get();
		
		
		String responseAsString = response.readEntity(String.class);
		System.out.println(responseAsString);
		 try {
			 json = new JSONObject(responseAsString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
		//getWalletDetails(responseAsString);
	}
	

	
	public void getWalletDetails(String responseAsString,String username) {
	Scanner sc = new Scanner(System.in);
	String uploadUrl = "https://cdta-dev3.gfcp.io/services/data-api/mobile/wallets/for/"+username+"?tenant=CDTA";
		Client client = ClientBuilder.newClient();
		Response response = client.target(uploadUrl).request().get();
	}


	
	
	
	
	
}
