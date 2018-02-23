package com.genfare.farebox.optionsImpl;

import java.util.Properties;

import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.farebox.clientrequest.AutoloadProcess;
import com.genfare.farebox.clientrequest.DeviceAuthentication;
import com.genfare.farebox.clientrequest.WalletElectronicId;
import com.genfare.farebox.main.EnvironmentSetting;
import com.genfare.farebox.util.PropertiesRetrieve;

public class AutoloadOptImpl {

	public void autoloadProcess(String cardNumber, String sequenceNumber) {
		PropertiesRetrieve propertiesRetrieve = new PropertiesRetrieve();
		Properties property = propertiesRetrieve.getProperties(); 
		String tenant=EnvironmentSetting.getTenant().toLowerCase();
		WalletElectronicId walletElectronicId = new WalletElectronicId();
		String cardElectronicId=walletElectronicId.getElectronicId(cardNumber);
		if(cardElectronicId!=null)
		{
		DeviceAuthentication deviceAuthentication = new DeviceAuthentication();
		String serialNumber = property.getProperty(tenant+"."+EnvironmentSetting.getEnv()+".fbxno");
		String password = property.getProperty(tenant+"."+EnvironmentSetting.getEnv()+".pwd");
		DeviceAuthResponse deviceAuthResponse = deviceAuthentication.authenticate(serialNumber, password);
		if (deviceAuthResponse != null) {
			
			AutoloadProcess uploadRecords = new AutoloadProcess();
			String response = uploadRecords.uploadRecords(deviceAuthResponse,cardElectronicId,sequenceNumber);
			System.out.println(response);

		}

	}
		else
		{
			System.out.println("Invalid CardNumber:"+cardNumber);
		}
	}

}
