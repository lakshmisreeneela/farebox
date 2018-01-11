package com.genfare.farebox.clientrequest;

import java.util.Scanner;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import javax.ws.rs.client.Client;

public class WalletDetails {
	
	public static final String AUTH_HEADER_PROPERTY = "Authorization";
	public static final String AUTH_HEADER_PREFIX = "Basic";
	
	
	
	
	public JSONObject  getWalletDetails(String access_token,String token_type,String username) {
	
	String uploadUrl = "https://cdta-dev3.gfcp.io/services/data-api/mobile/wallets/for/"+username+"?tenant=CDTA";
	MultivaluedMap<String, Object> head = new MultivaluedHashMap<String, Object>();
	head.add(AUTH_HEADER_PROPERTY,token_type+" "+access_token);
	 JSONObject json = null;
		Client client = ClientBuilder.newClient();
		Response response = client.target(uploadUrl).request().headers(head).get();
		String responseAsString = response.readEntity(String.class);
		 if(response.getStatus() == 200)
		 {
		 try {
			json = new JSONObject(responseAsString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 }
		return json;
	}


	
	
	
	
	
}
