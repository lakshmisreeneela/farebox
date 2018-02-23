package com.genfare.farebox.clientrequest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.genfare.farebox.main.EnvironmentSetting;

public class WalletContents {
	public static final String AUTH_HEADER_PROPERTY = "Authorization";
	JSONArray json = null;

	
	
	public JSONArray getWalletContents(String cardNumber) {
		Tokens tokens = new Tokens();
		JSONObject jSONObject = tokens.getToken();
		
		 if (jSONObject != null) {
			try {
				String access_token = jSONObject.getString("access_token");
				String token_type = jSONObject.getString("token_type");
				json = post(cardNumber, access_token, token_type);	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	
	
	
	private JSONArray post(String cardNumber, String access_token, String token_type) {
		String env = EnvironmentSetting.getEnv();

		String sentURL = "https://api." + env + ".gfcp.io/services/data-api/v1/wallets/types/smart_card/identifiers/"
				+ cardNumber + "/contents?tenant=CDTA";

		String authorizationHeader = token_type + " " + access_token;

		Client client = ClientBuilder.newClient();
		try {
			MultivaluedMap<String, Object> head = new MultivaluedHashMap<String, Object>();
			head.add(AUTH_HEADER_PROPERTY, authorizationHeader);
			head.add("Accept", "application/json");
			Response response = client.target(sentURL).request().headers(head).get();
			String responseAsString = response.readEntity(String.class);
			
			if (response.getStatus() == 200) {

				json = new JSONArray(responseAsString);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;                  
	}
	
	
}
