package com.genfare.farebox.optionsImpl;

import java.util.Properties;

import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.farebox.clientrequest.DeviceAuthentication;
import com.genfare.farebox.clientrequest.RiderShip;
import com.genfare.farebox.main.EnvironmentSetting;
import com.genfare.farebox.util.PropertiesRetrieve;

public class RiderShipImpl { 

	public void riderShipProcess(String cardNumber,String ammount, String sequenceNumber) {
		PropertiesRetrieve propertiesRetrieve = new PropertiesRetrieve();
		Properties property = propertiesRetrieve.getProperties();
		String tenant=EnvironmentSetting.getTenant().toLowerCase();
		ElectronicID electronicID = new ElectronicID();
		String cardElectronicId=electronicID.getElectronicId(cardNumber);
		if(cardElectronicId!=null)
		{
		
		
		DeviceAuthentication deviceAuthentication = new DeviceAuthentication();
		String serialNumber = property.getProperty(tenant+"."+EnvironmentSetting.getEnv()+".fbxno");
		String password = property.getProperty(tenant+"."+EnvironmentSetting.getEnv()+".pwd");
		DeviceAuthResponse deviceAuthResponse = deviceAuthentication.authenticate(serialNumber, password);
		if (deviceAuthResponse != null) {
			RiderShip uploadRecords = new RiderShip();
			String response = uploadRecords.uploadRecords(deviceAuthResponse,cardNumber,cardElectronicId,ammount,sequenceNumber);
			
			System.out.println(response);

		}
		}
		else
		{
			System.out.println("Invalid CardNumber:"+cardNumber);
		}

	}
}
