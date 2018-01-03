package com.genfare.farebox.main;

import java.util.Scanner;
import java.util.logging.Logger;

import com.amazonaws.util.json.JSONObject;
import com.genfare.farebox.clientrequest.DeviceAuthentication;
import com.genfare.farebox.clientrequest.UploadRecords;
import com.genfare.farebox.clientrequest.WalletDetails;
import com.genfare.farebox.response.DeviceAuthResponse;

public class FareBox {

	private static final Logger log = Logger.getLogger(FareBox.class.getName());

	
	public static void main(String[] args) {
		WalletDetails walletDetails = new WalletDetails();
		
		Scanner sc = new Scanner(System.in);
		System.out.println("please enter Username");
		String username = sc.nextLine();
		JSONObject response = WalletDetails.userLogin(username);
		if(response != null)
		{
		//walletDetails.getWalletDetails(response.getString("access_token"));
			
		}
		
		
		
		DeviceAuthentication authentication = new DeviceAuthentication();
		DeviceAuthResponse deviceAuthResponse = authentication.authenticate();
		if (deviceAuthResponse != null) {
			
			
			UploadRecords uploadRecords = new UploadRecords();
			
				uploadRecords.uploadRecords(deviceAuthResponse);
			
		}

	}
}


