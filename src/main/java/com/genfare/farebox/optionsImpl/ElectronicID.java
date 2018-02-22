package com.genfare.farebox.optionsImpl;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
			JSONArray products =  jSONObject.getJSONArray("contents");
			System.out.println("PRODUCTS");
			for (int i = 0; i < products.length(); i++)
			{
				
				
				JSONObject product = jSONArray.getJSONObject(i);
				System.out.println("Product-"+i);
				System.out.println("Type : "+product.get("type"));
				System.out.println("Description : "+product.get("description"));
				System.out.println("Balance : "+product.get("balance"));
			}
			
			
		} 
		}catch (JSONException e) {
			e.printStackTrace();
		}
	}
	return electronicid;
	}
}
	
