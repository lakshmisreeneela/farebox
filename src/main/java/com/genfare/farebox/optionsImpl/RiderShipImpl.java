package com.genfare.farebox.optionsImpl;

import java.util.Properties;

import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.farebox.clientrequest.DeviceAuthentication;
import com.genfare.farebox.clientrequest.RiderShip;
import com.genfare.farebox.main.EnvironmentSetting;
import com.genfare.farebox.util.PropertiesRetrieve;

public class RiderShipImpl { 

	public void riderShipProcess(String cardNumber, String sequenceNumber) {
		PropertiesRetrieve propertiesRetrieve = new PropertiesRetrieve();
		Properties property = propertiesRetrieve.getProperties();
		
		ElectronicID electronicID = new ElectronicID();
		String cardElectronicId=electronicID.getElectronicId(cardNumber);
		if(cardElectronicId!=null)
		{
		
		
		DeviceAuthentication deviceAuthentication = new DeviceAuthentication();
		String serialNumber = property.getProperty(EnvironmentSetting.getEnv()+".fbxno");
		String password = property.getProperty(EnvironmentSetting.getEnv()+".pwd");
		DeviceAuthResponse deviceAuthResponse = deviceAuthentication.authenticate(serialNumber, password);
		if (deviceAuthResponse != null) {
			RiderShip uploadRecords = new RiderShip();
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
