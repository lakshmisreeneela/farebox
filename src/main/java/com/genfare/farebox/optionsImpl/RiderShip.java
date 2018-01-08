package com.genfare.farebox.optionsImpl;

import java.util.Properties;

import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.farebox.clientrequest.DeviceAuthentication;
import com.genfare.farebox.clientrequest.UploadRecords;
import com.genfare.farebox.util.PropertiesRetrieve;

public class RiderShip {

	public void riderShipProcess(String electronicId, String sequenceNumber) {
		DeviceAuthentication deviceAuthentication = new DeviceAuthentication();
		PropertiesRetrieve propertiesRetrieve = new PropertiesRetrieve();
		Properties property = propertiesRetrieve.getProperties();
		String serialNumber = property.getProperty("fareBoxSerialNumber");
		String password = property.getProperty("fareBoxPassword");
		DeviceAuthResponse deviceAuthResponse = deviceAuthentication.authenticate(serialNumber,password);
		if (deviceAuthResponse != null) {
			UploadRecords uploadRecords = new UploadRecords();
			String response = uploadRecords.uploadRecords(deviceAuthResponse);
			System.out.println(response);
			

		}

	}
}
