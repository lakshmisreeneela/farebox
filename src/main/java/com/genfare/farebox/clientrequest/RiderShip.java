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
	private String identifier = "";
	
	
	public String uploadRecords(DeviceAuthResponse deviceAuthResponse,String cardNumber,String electronicId,String identifier,String amount, String sequenceNumber) {
		this.identifier = identifier;
		int amountTOBeCharge = Integer.parseInt(amount);
		AwsCredentials awsCredentials = deviceAuthResponse.getAws().getCredentials();
		String accessKey = awsCredentials.getAccessKey();
		String secretKey = awsCredentials.getSecretKey();
		String sessionId = awsCredentials.getSessionId();
		byte[] authorizationBytes = (accessKey + " | " + secretKey + " | " + sessionId).getBytes();
		String awsAuthorizationKey = new String(Base64.encodeBytes(authorizationBytes));
		String uploadUrlString = "https://"+EnvironmentSetting.getEnvironment()+"/services/device/authenticated/v2/event";
		Usage usage = new Usage();
		DeviceEventAPI deviceEventAPI =usage.getDeviceHeader();
		deviceEventAPI = getUsageRecords(deviceEventAPI,cardNumber,electronicId,amountTOBeCharge,sequenceNumber);
		if(deviceEventAPI != null)
		{
		String xml = usage.makeXml(deviceEventAPI);
		return usage.post(uploadUrlString, awsAuthorizationKey, xml);
		}
		return null;

	}


	
	
	private DeviceEventAPI getUsageRecords(DeviceEventAPI deviceEventAPI,String cardNumber, String electronicId,int amount, String sequenceNumber) {
		
		ArrayList<UsageRecordType> usageRecordTypes = new ArrayList<UsageRecordType>();
		DateType dateType = deviceEventAPI.getHeader().getDateSent();
		UsageRecordType usageRecordType = prepareUsageRecord(cardNumber,electronicId,amount,dateType);
		
		if(usageRecordType != null)
		{
		BigInteger sum = BigInteger.valueOf(0);
		sum = sum.add(BigInteger.valueOf(Long.parseLong(sequenceNumber)));
		usageRecordType.setSequenceNumber(sum);
		usageRecordTypes.add(usageRecordType);
		deviceEventAPI.setRecords(new RecordsType());
		deviceEventAPI.getRecords().setUsages(new RecordsType.Usages());
		deviceEventAPI.getRecords().getUsages().getUsage().add(usageRecordType);
		return deviceEventAPI;
		}
		
		return null;
		
}



	private UsageRecordType prepareUsageRecord(String cardNumber,String electronicId,int amount, DateType dateType) {
		
		UsageRecordType usageRecordType = new UsageRecordType();
		
		WalletContents walletContents = new WalletContents();
		JSONArray jSONArray = walletContents.getWalletContents(cardNumber);
		usageRecordType = addRequiredFields(usageRecordType,jSONArray,amount);
		
		if(usageRecordType!= null)
		{
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
		}
		
		
		return usageRecordType;
	}




	
	private UsageRecordType addRequiredFields(UsageRecordType usageRecordType, JSONArray products, int amount) {
		boolean productFound = false;
	
		for (int i = 0; i < products.length(); i++) {
			JSONObject product = products.getJSONObject(i);
			BigDecimal balance = new BigDecimal(((String)product.get("balance")));
			int bal = balance.toBigInteger().intValueExact();
			
			if (product.get("identifier").equals(identifier)&&(amount<=bal)) {
				productFound = true;
				usageRecordType.setDesignator(product.getInt("designator"));
				Integer group = (Integer)product.get("group");
				usageRecordType.setGroup(group.byteValue());
				usageRecordType.setTTP(product.getInt("ttp"));
				Integer slot = (Integer)product.get("slot");
				usageRecordType.setSlot(slot.byteValue());
				usageRecordType.setAmountCharged(new BigDecimal(amount));
				usageRecordType.setAmountRemaining(new BigDecimal(bal -amount));
				break;
			}
			
			}
		if(productFound==false)
		{
			System.out.println("Don't have enough balance in the product");
			return null;
		}

		return usageRecordType;
	}

}

