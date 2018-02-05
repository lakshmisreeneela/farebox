package com.genfare.farebox.clientrequest;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Properties;

import org.jboss.resteasy.util.Base64;

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
	
	
	
	public String uploadRecords(DeviceAuthResponse deviceAuthResponse,String electronicId, String sequenceNumber) {

		AwsCredentials awsCredentials = deviceAuthResponse.getAws().getCredentials();
		String accessKey = awsCredentials.getAccessKey();
		String secretKey = awsCredentials.getSecretKey();
		String sessionId = awsCredentials.getSessionId();
		byte[] authorizationBytes = (accessKey + " | " + secretKey + " | " + sessionId).getBytes();
		String awsAuthorizationKey = new String(Base64.encodeBytes(authorizationBytes));
		String uploadUrlString = "https://"+EnvironmentSetting.getEnvironment()+"/services/device/authenticated/v2/event";
		Usage usage = new Usage();
		DeviceEventAPI deviceEventAPI =usage.getDeviceHeader();
		deviceEventAPI = getUsageRecords(deviceEventAPI,electronicId,sequenceNumber);
		
		String xml = usage.makeXml(deviceEventAPI);
		return usage.post(uploadUrlString, awsAuthorizationKey, xml);

	}


	
	
	private DeviceEventAPI getUsageRecords(DeviceEventAPI deviceEventAPI, String electronicId, String sequenceNumber) {
		
		ArrayList<UsageRecordType> usageRecordTypes = new ArrayList<UsageRecordType>();
		DateType dateType = deviceEventAPI.getHeader().getDateSent();
		UsageRecordType usageRecordType = prepareUsageRecord(electronicId,dateType);
		
		
		BigInteger sum = BigInteger.valueOf(0);
		sum = sum.add(BigInteger.valueOf(Long.parseLong(sequenceNumber)));
		usageRecordType.setSequenceNumber(sum);
		usageRecordTypes.add(usageRecordType);
		deviceEventAPI.setRecords(new RecordsType());
		deviceEventAPI.getRecords().setUsages(new RecordsType.Usages());
		deviceEventAPI.getRecords().getUsages().getUsage().add(usageRecordType);
		return deviceEventAPI;
		
}



	private UsageRecordType prepareUsageRecord(String electronicId, DateType dateType) {
		
		UsageRecordType usageRecordType = new UsageRecordType();
		usageRecordType.setTerminalNumber(fbxNo);
		usageRecordType.setTimestamp(new DateType());
		usageRecordType.setTerminalType(property.getProperty("deviceType"));
		usageRecordType.setDesignator(Integer.parseInt(property.getProperty("Designator")));
		usageRecordType.setRouteId(Integer.parseInt(property.getProperty("RouteId")));
		usageRecordType.setGroup((byte) Integer.parseInt(property.getProperty("Group")));
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
		usageRecordType.setTTP(62);

		usageRecordType.setDateOfUsage(dateType);
		usageRecordType.setTimestamp(dateType);
		return usageRecordType;
	}



}

