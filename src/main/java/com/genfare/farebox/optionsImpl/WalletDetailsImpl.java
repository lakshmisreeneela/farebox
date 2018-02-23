package com.genfare.farebox.optionsImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import com.genfare.farebox.clientrequest.WalletContents;
import com.genfare.farebox.clientrequest.WalletElectronicId;

public class WalletDetailsImpl {

	public void getWalletDetails(String cardNumber) {

		WalletElectronicId walletElectronicId = new WalletElectronicId();
		String electronic_id = walletElectronicId.getElectronicId(cardNumber);
		System.out.println("EleectronicId :" + electronic_id);
		WalletContents walletContents = new WalletContents();
		JSONArray products = walletContents.getWalletContents(cardNumber);
		System.out.println("PRODUCTS");
		for (int i = 0; i < products.length(); i++) {
			JSONObject product = products.getJSONObject(i);
			System.out.println("--------------------------------------");
			System.out.println("Product-" + (i + 1));
			System.out.println("--------------------------------------");
			System.out.println("Identifier : " + product.get("identifier"));
			System.out.println("Type : " + product.get("type"));
			System.out.println("Description : " + product.get("description"));
			System.out.println("Balance : " + product.get("balance"));

		}
	}
}
