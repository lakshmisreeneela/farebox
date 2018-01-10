package com.genfare.farebox.optionsImpl;

import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.farebox.clientrequest.DeviceAuthentication;
import com.genfare.farebox.main.EnvironmentSetting;

public class DeviceAuth {

	
	public String authenticate(String fareBoxSerialNumber,String fareBoxPassword) {
		
		DeviceAuthentication deviceAuthentication = new DeviceAuthentication();
		DeviceAuthResponse deviceAuthResponse = deviceAuthentication.authenticate(fareBoxSerialNumber, fareBoxPassword);
		if(deviceAuthResponse != null)
		{
			EnvironmentSetting.setFbSerialNumber(fareBoxSerialNumber);
			EnvironmentSetting.setFbPassword(fareBoxPassword);
			return "authentication successfull";
		}
		return "failed to authenticate";
	}
	
	
	
}
