package com.genfare.farebox.optionsImpl;

import com.amazonaws.util.json.JSONObject;
import com.genfare.farebox.clientrequest.UserLogin;

public class UserLoginOptImpl {

	public String userLogin(String username,String password)
	{
		
		UserLogin userLogin = new UserLogin();
		JSONObject jSONObject = userLogin.userLogin(username, password);
		if(jSONObject != null)
		{
			System.out.println("login successfull");
			return "login Successfull";
		}
		else
		{
			return "invalid username and password";
		}
		
		}
	
	
}
