package com.genfare.farebox.main;

import java.util.Scanner;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.farebox.clientrequest.DeviceAuthentication;
import com.genfare.farebox.clientrequest.UploadRecords;
import com.genfare.farebox.clientrequest.WalletDetails;

public class FareBox {

	private static final Logger log = Logger.getLogger(FareBox.class.getName());

	
	public static void main(String[] args) {
		Options options = new Options();
		options.addOption("auth", "authenticate",false,"will run myCommand()" );
		DeviceAuthentication authentication = new DeviceAuthentication();
		try {
			CommandLine line = new BasicParser().parse(options, args );
			if(line.hasOption("auth"))
			{
					String repeat = line.getOptionValue("auth");
					String[] a = repeat.split("");
					//DeviceAuthResponse deviceAuthResponse = authentication.authenticate(a[0],a[1]);
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
	WalletDetails walletDetails = new WalletDetails();
	
		Scanner sc = new Scanner(System.in);
		System.out.println("please enter Username");
		String username = sc.nextLine();
		JSONObject response = WalletDetails.userLogin(username);
		if(response != null)
		{			try {
				walletDetails.getWalletDetails(response.getString("access_token"),response.getString("token_type"),username);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	
	
//		DeviceAuthResponse deviceAuthResponse = authentication.authenticate();
//		if (deviceAuthResponse != null) {
//			
//			
//			UploadRecords uploadRecords = new UploadRecords();
//			
//				uploadRecords.uploadRecords(deviceAuthResponse);
//			
//		}

	}
}


