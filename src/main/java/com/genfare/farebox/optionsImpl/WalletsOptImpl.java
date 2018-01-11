package com.genfare.farebox.optionsImpl;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.genfare.farebox.clientrequest.UserLogin;
import com.genfare.farebox.clientrequest.WalletDetails;

public class WalletsOptImpl {

	public void getWallets(String username, String password) {
		UserLogin userLogin = new UserLogin();
		JSONObject jSONObject = userLogin.userLogin(username, password);
		if (jSONObject != null) {
			try {
				WalletDetails walletDetails = new WalletDetails();
				JSONObject json = walletDetails.getWalletDetails(jSONObject.getString("access_token"),
						jSONObject.getString("token_type"), username);

				JSONArray result = json.getJSONArray("result");
				if(result.length() == 0)
				{
					System.out.println("no wallets found for user: "+username);
				}
				else
				{
					
				}
				

			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("");
		}
	}

}
