package com.genfare.farebox.clientrequest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.genfare.farebox.main.EnvironmentSetting;

public class WalletElectronicId {
	public static final String AUTH_HEADER_PROPERTY = "Authorization";
	JSONObject json = null;
	
	
	public String getElectronicId(String cardNumber) {
		Tokens tokens = new Tokens();
		JSONObject jSONObject = tokens.getToken();
		String electronicid = null;
		if (jSONObject != null) {
			String access_token = jSONObject.getString("access_token");
			String token_type = jSONObject.getString("token_type");
			post(access_token, token_type, cardNumber);
			if (json != null) {
				electronicid = getEid(json);
			}
		}
		return electronicid;
	}
			
			
			
			
	private JSONObject post(String access_token, String token_type, String cardNumber) {
		String env = EnvironmentSetting.getEnv();
		String uploadURL = "https://api." + env + ".gfcp.io/services/data-api/v1/wallets/types/smart_card/identifiers/"
				+ cardNumber + "?tenant=CDTA";
		String authorizationHeader = token_type + " " + access_token;
		Client client = ClientBuilder.newClient();
		try {
			MultivaluedMap<String, Object> head = new MultivaluedHashMap<String, Object>();
			head.add(AUTH_HEADER_PROPERTY, authorizationHeader);
			head.add("Accept", "application/json");

			Response response = client.target(uploadURL).request().headers(head).get();
			if (response.getStatus() == 200) {
				String responseAsString = response.readEntity(String.class);
				json = new JSONObject(responseAsString);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return json;
	}


	
	
	
	
	private String getEid(JSONObject jSONObject) {
		String electronicid = null;
		if (jSONObject != null) {
			JSONArray jSONArray = jSONObject.getJSONArray("identifiers");
			for (int i = 0; i < jSONArray.length(); i++) {
				JSONObject jSONArray2 = jSONArray.getJSONObject(i);
				if (jSONArray2.get("type").equals("electronic_id")) {
					electronicid = (String) jSONArray2.get("identifier");
					break;
				}
			}

		}
		return electronicid;

	}


}
