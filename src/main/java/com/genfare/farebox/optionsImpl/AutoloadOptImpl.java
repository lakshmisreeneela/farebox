package com.genfare.farebox.optionsImpl;

import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.farebox.clientrequest.AutoloadProcess;
import com.genfare.farebox.clientrequest.DeviceAuthentication;
import com.genfare.farebox.main.EnvironmentSetting;

public class AutoloadOptImpl {

	public void autoloadProcess(String electronicId, String sequenceNumber) {
		DeviceAuthentication deviceAuthentication = new DeviceAuthentication();
		String serialNumber = EnvironmentSetting.getFbSerialNumber();
		String password = EnvironmentSetting.getFbPassword();
		DeviceAuthResponse deviceAuthResponse = deviceAuthentication.authenticate(serialNumber, password);
		if (deviceAuthResponse != null) {
			
			AutoloadProcess uploadRecords = new AutoloadProcess();
			String response = uploadRecords.uploadRecords(deviceAuthResponse,electronicId,sequenceNumber);
			System.out.println(response);

		}

	}

}
