package com.genfare.farebox.optionsImpl;

import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.farebox.clientrequest.DeviceAuthentication;
import com.genfare.farebox.clientrequest.RiderShip;
import com.genfare.farebox.main.EnvironmentSetting;

public class RiderShipImpl { 

	public void riderShipProcess(String cardNumber, String sequenceNumber) {
		
		ElectronicID electronicID = new ElectronicID();
		String cardElectronicId=electronicID.getElectronicId(cardNumber);
		if(cardElectronicId!=null)
		{
		
		
		DeviceAuthentication deviceAuthentication = new DeviceAuthentication();
		String serialNumber = EnvironmentSetting.getFbSerialNumber();
		String password = EnvironmentSetting.getFbPassword();
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
