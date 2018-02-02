package com.genfare.farebox.optionsImpl;

import java.util.Properties;

import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.farebox.clientrequest.DeviceAuthentication;
import com.genfare.farebox.main.EnvironmentSetting;
import com.genfare.farebox.util.PropertiesRetrieve;

public class DeviceAuthOptImpl {

	
	public String authenticate() {
		
		PropertiesRetrieve propertiesRetrieve = new PropertiesRetrieve();
		Properties property = propertiesRetrieve.getProperties(); 
		String tenant=EnvironmentSetting.getTenant().toLowerCase();
		String serialNumber = property.getProperty(tenant+"."+EnvironmentSetting.getEnv()+".fbxno");
		String password = property.getProperty(tenant+"."+EnvironmentSetting.getEnv()+".pwd");
		
		
		DeviceAuthentication deviceAuthentication = new DeviceAuthentication();
		DeviceAuthResponse deviceAuthResponse = deviceAuthentication.authenticate(serialNumber,password);
		if(deviceAuthResponse != null)
		{
			EnvironmentSetting.setFbSerialNumber(serialNumber);
			EnvironmentSetting.setFbPassword(password);
			return "authentication successfull";
		}
		return "failed to authenticate";
	}
	
	
	
}
