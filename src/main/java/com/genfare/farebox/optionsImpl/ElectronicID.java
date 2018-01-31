package com.genfare.farebox.optionsImpl;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.genfare.farebox.clientrequest.Tokens;
import com.genfare.farebox.clientrequest.WalletElectronicId;

public class ElectronicID {
public String getElectronicId(String cardNumber)
{
	Tokens tokens = new Tokens();
	JSONObject jSONObject = tokens.getToken();
	String electronicid = null;
	if(jSONObject != null)
	{
		try {
			String access_token = jSONObject.getString("access_token");
			String token_type = jSONObject.getString("token_type");
			WalletElectronicId walletElectronicId = new WalletElectronicId();
			jSONObject = walletElectronicId.getElectronicId(access_token, token_type, cardNumber);
			if(jSONObject != null)
			{
			JSONArray jSONArray =  jSONObject.getJSONArray("identifiers");
			 for (int i = 0; i < jSONArray.length(); i++) {
				 JSONObject jSONArray2 = jSONArray.getJSONObject(i);
				 if(jSONArray2.get("type").equals("electronic_id"))
				 {
					 electronicid = (String) jSONArray2.get("identifier");
					 break;
				 }
			 }
			System.out.println("Card Electronic_Id : "+electronicid);
			
		} 
		}catch (JSONException e) {
			e.printStackTrace();
		}
	}
	return electronicid;
	}
}
	
