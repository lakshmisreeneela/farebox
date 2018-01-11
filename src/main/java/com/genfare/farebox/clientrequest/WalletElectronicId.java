package com.genfare.farebox.clientrequest;

import com.amazonaws.util.json.JSONObject;

public class WalletElectronicId {
	
public void getWalletElectronicId(String cardNumber)
{
	String url ="https://api.intg.gfcp.io/services/data-api/v1/wallets/types/smart_card/identifiers/0000000215?tenant=CDTA"
	Tokens tokens = new Tokens();
	JSONObject jSONObject = tokens.getToken();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
	jSONObject.getString("access_token");
	
}
	
}
