package com.genfare.farebox.clientrequest;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Properties;

import org.jboss.resteasy.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import com.genfare.cloud.device.common.DateType;
import com.genfare.cloud.device.record.DeviceEventAPI;
import com.genfare.cloud.device.record.RecordsType;
import com.genfare.cloud.device.record.UsageRecordType;
import com.genfare.cloud.osgi.device.auth.response.AwsResponse.AwsCredentials;
import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.farebox.main.EnvironmentSetting;
import com.genfare.farebox.util.PropertiesRetrieve;
import com.genfare.farebox.util.Usage;

public class RiderShip {

	
	static final String AUTH_HEADER_PROPERTY = "Authorization";
	InputStream input = null;
	
	private PropertiesRetrieve propertiesRetrieve = new PropertiesRetrieve();
	private Properties property = propertiesRetrieve.getProperties(); 
	String tenant=EnvironmentSetting.getTenant().toLowerCase();
	DateType dateType = new DateType();
	String fbxNo = property.getProperty(tenant+"."+EnvironmentSetting.getEnv()+".fbxno");
	
	
	
	public String uploadRecords(DeviceAuthResponse deviceAuthResponse,String cardNumber,String electronicId,String amount, String sequenceNumber) {

		AwsCredentials awsCredentials = deviceAuthResponse.getAws().getCredentials();
		String accessKey = awsCredentials.getAccessKey();
		String secretKey = awsCredentials.getSecretKey();
		String sessionId = awsCredentials.getSessionId();
		byte[] authorizationBytes = (accessKey + " | " + secretKey + " | " + sessionId).getBytes();
		String awsAuthorizationKey = new String(Base64.encodeBytes(authorizationBytes));
		String uploadUrlString = "https://"+EnvironmentSetting.getEnvironment()+"/services/device/authenticated/v2/event";
		Usage usage = new Usage();
		DeviceEventAPI deviceEventAPI =usage.getDeviceHeader();
		deviceEventAPI = getUsageRecords(deviceEventAPI,cardNumber,electronicId,amount,sequenceNumber);
		
		String xml = usage.makeXml(deviceEventAPI);
		return usage.post(uploadUrlString, awsAuthorizationKey, xml);

	}


	
	
	private DeviceEventAPI getUsageRecords(DeviceEventAPI deviceEventAPI,String cardNumber, String electronicId,String amount, String sequenceNumber) {
		
		ArrayList<UsageRecordType> usageRecordTypes = new ArrayList<UsageRecordType>();
		DateType dateType = deviceEventAPI.getHeader().getDateSent();
		UsageRecordType usageRecordType = prepareUsageRecord(cardNumber,electronicId,amount,dateType);
		
		
		BigInteger sum = BigInteger.valueOf(0);
		sum = sum.add(BigInteger.valueOf(Long.parseLong(sequenceNumber)));
		usageRecordType.setSequenceNumber(sum);
		usageRecordTypes.add(usageRecordType);
		deviceEventAPI.setRecords(new RecordsType());
		deviceEventAPI.getRecords().setUsages(new RecordsType.Usages());
		deviceEventAPI.getRecords().getUsages().getUsage().add(usageRecordType);
		return deviceEventAPI;
		
}



	private UsageRecordType prepareUsageRecord(String cardNumber,String electronicId,String amount, DateType dateType) {
		
		UsageRecordType usageRecordType = new UsageRecordType();
		
		WalletContents walletContents = new WalletContents();
		JSONObject  jSONObject = walletContents.getWalletContents(cardNumber);
		addRequiredFields(usageRecordType,jSONObject,amount);
		
		
		usageRecordType.setTerminalNumber(fbxNo);
		usageRecordType.setTimestamp(dateType);
		usageRecordType.setTerminalType(property.getProperty("deviceType"));
		usageRecordType.setRouteId(Integer.parseInt(property.getProperty("RouteId")));
		usageRecordType.setOperatorId(Integer.parseInt(property.getProperty("OperatorId")));
		usageRecordType.setAmountCharged(new BigDecimal(0.00));
		usageRecordType.setAmountRemaining(new BigDecimal(0.00));

		usageRecordType.setElectronicId(electronicId);
		usageRecordType.setPendingCount(0);
		usageRecordType.setPayGoType(0);
		usageRecordType.setLatitude("0.0000");
		usageRecordType.setLongitude("0.0000");
		usageRecordType.setPaymenttype("EXISTING_FARECARD");
		usageRecordType.setFareset(Integer.parseInt(property.getProperty("fareset")));
		
		usageRecordType.setDateOfUsage(dateType);
		usageRecordType.setTimestamp(dateType);
		
		
		return usageRecordType;
	}




	
	private UsageRecordType addRequiredFields(UsageRecordType usageRecordType, JSONObject jSONObject, String amount) {

		JSONArray products = jSONObject.getJSONArray("content");

		int designator = 0;
		byte group = 0;
		int ttp = 0;
		byte slot = 0;
		BigDecimal balance;

		for (int i = 0; i < products.length(); i++) {
			JSONObject product = products.getJSONObject(i);
			balance = (BigDecimal) product.get("balance");

			if ((product.get("type").equals("stored_value") || product.get("type").equals("period_pass")
					|| product.get("type").equals("stored_ride"))) {

				designator = product.getInt("designator");
				group = (byte) product.getInt("group");
				ttp = product.getInt("ttp");
				slot = (byte) product.getInt("slot");
				validateBalance(amount, balance);
				break;
			}
			
			usageRecordType.setDesignator(designator);
			usageRecordType.setGroup(group);
			usageRecordType.setTTP(ttp);
			usageRecordType.setSlot(slot);
		}
		return usageRecordType;

	}




	private void validateBalance(String amount, BigDecimal balance) {
		String amountToBeCharged;
		String ramainingAmount;
		
		if(new BigDecimal(amount) <=balance)
		{
			
		}
		
		
		
	}


}

