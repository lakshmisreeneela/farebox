package com.genfare.farebox.main;

import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeConfigurationException;

public class FareBox {

	private static final Logger log = Logger.getLogger(FareBox.class.getName());

	public static void main(String[] args) {

		DeviceAuthentication authentication = new DeviceAuthentication();
		DeviceAuthResponse deviceAuthResponse = authentication.authenticate();
		if (deviceAuthResponse != null) {
			UploadRecords uploadRecords = new UploadRecords();
			try {
				uploadRecords.uploadRecords(deviceAuthResponse);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
		}

	}

}
