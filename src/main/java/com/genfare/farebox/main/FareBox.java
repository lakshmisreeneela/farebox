package com.genfare.farebox.main;

import java.util.logging.Logger;


import com.genfare.farebox.response.DeviceAuthResponse;

public class FareBox {

	private static final Logger log = Logger.getLogger(FareBox.class.getName());

	public static void main(String[] args) {

		DeviceAuthentication authentication = new DeviceAuthentication();
		DeviceAuthResponse deviceAuthResponse = authentication.authenticate();
		if (deviceAuthResponse != null) {
			UploadRecords uploadRecords = new UploadRecords();
			
				uploadRecords.uploadRecords(deviceAuthResponse);
			
		}

	}
}


