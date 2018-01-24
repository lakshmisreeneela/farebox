package com.genfare.farebox.clientrequest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.amazonaws.util.json.JSONObject;
import com.genfare.farebox.main.EnvironmentSetting;

public class WalletElectronicId {
	public static final String AUTH_HEADER_PROPERTY = "Authorization";
	JSONObject json = null;
	public JSONObject getElectronicId(String auth_token, String tokenType, String cardNumber) {
		String environment = EnvironmentSetting.getEnvironment();
		int index = environment.indexOf('-');
		if(index != -1)
		{
		environment = environment.substring(index+1);
		}
		
		String uploadURL = "https://api."+environment+"/services/data-api/v1/wallets/types/smart_card/identifiers/"+cardNumber+"?tenant=CDTA";
		String authorizationHeader = tokenType + " " + auth_token;
		Client client = ClientBuilder.newClient();
		try {
			MultivaluedMap<String, Object> head = new MultivaluedHashMap<String, Object>();
			head.add(AUTH_HEADER_PROPERTY, authorizationHeader);
			head.add("Accept", "application/json");

			Response response = client.target(uploadURL).request().headers(head).get();
			if (response.getStatus() == 200) {
				String responseAsString = response.readEntity(String.class);
				json = new JSONObject(responseAsString);
//				if(json != null)
//				{
//					
//				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;

	}
	
}
