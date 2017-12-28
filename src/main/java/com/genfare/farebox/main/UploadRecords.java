package com.genfare.farebox.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.genfare.apache.impl.Base64;
import com.genfare.cloud.device.common.DateType;
import com.genfare.cloud.device.header.DeviceHeaderType;
import com.genfare.cloud.device.record.DeviceEventAPI;
import com.genfare.cloud.device.record.RecordsType;
import com.genfare.cloud.device.record.UsageRecordType;
import com.genfare.farebox.main.AwsResponse.AwsCredentials;

@SuppressWarnings("restriction")
public class UploadRecords {

	
	private static final Logger log = Logger.getLogger(UploadRecords.class.getName());
	
	static final String AUTH_HEADER_PROPERTY = "Authorization";
	InputStream input = null;
	Properties property = new Properties();
	
	
		public void uploadRecords(DeviceAuthResponse deviceAuthResponse) throws IOException, DatatypeConfigurationException {

			
			
		AwsCredentials awsCredentials = deviceAuthResponse.getAws().getCredentials();
		String accessKey = awsCredentials.getAccessKey();
		String secretKey = awsCredentials.getSecretKey();
		String sessionId = awsCredentials.getSessionId();
		byte[] authorizationBytes = (accessKey + " | " + secretKey +" | "+sessionId).getBytes();
		String awsAuthorizationKey = new String(Base64.encodeBase64(authorizationBytes));
		
		try {
			String filename = "device.properties";
			input = FareBox.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				// log.info("Sorry, unable to find " + filename);
			} else {
				property.load(input);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		String uploadUrlString = property.getProperty("uploadUrlString");
		DeviceEventAPI deviceEventAPI = new DeviceEventAPI();
		DeviceHeaderType deviceHeaderType = new DeviceHeaderType();
		DateType dateType =  new DateType();
		dateType.setLocalTime(false);
		
		XMLGregorianCalendar xmlgcal = getXMLGregorianCalendar(property.getProperty("dateofusage"));

		
//		GregorianCalendar gcal = new GregorianCalendar();
//	      XMLGregorianCalendar xmlgcal = DatatypeFactory.newInstance()
//	            .newXMLGregorianCalendar(gcal);
		dateType.setValue(xmlgcal);
		
		deviceHeaderType.setTenantName(property.getProperty("tenant"));
		deviceHeaderType.setEnvironment(property.getProperty("environment"));
		deviceHeaderType.setDeviceType(property.getProperty("deviceType"));
		deviceHeaderType.setOrganization(property.getProperty("organizationName"));
		deviceHeaderType.setSourceId(property.getProperty("fareBoxSerialNumber"));
		deviceHeaderType.setAction("UPDATE");
		deviceHeaderType.setTestMode(true);
		deviceHeaderType.setRetryNumber(0);
		deviceHeaderType.setDateSent(dateType);
		deviceHeaderType.setLocation(property.getProperty("Location"));
		deviceHeaderType.setMessageId(property.getProperty("MessageId"));
		deviceHeaderType.setCorrelationId(property.getProperty("CorrelationId"));
		
		deviceEventAPI.setHeader(deviceHeaderType);
		
		ArrayList<UsageRecordType> usageRecordTypes = new ArrayList<UsageRecordType>();
		UsageRecordType usageRecordType = new UsageRecordType();
		usageRecordType.setTerminalNumber(property.getProperty("fareBoxSerialNumber"));
		usageRecordType.setTimestamp(new DateType());
		usageRecordType.setTerminalType(property.getProperty("deviceType"));
		usageRecordType.setDesignator(29);
		usageRecordType.setRouteId(1);
		usageRecordType.setGroup((byte) 2);
		usageRecordType.setOperatorId(141);
		usageRecordType.setAmountCharged(new BigDecimal(0.00));
		usageRecordType.setAmountRemaining(new BigDecimal(0.00));
		
		Scanner sc = new Scanner(System.in);
		System.out.println("please enter card number");
		String electronicId = sc.nextLine();
		
		usageRecordType.setElectronicId(electronicId);
		usageRecordType.setPendingCount(0);
		usageRecordType.setPayGoType(1);
		usageRecordType.setLatitude("0.0000");
		usageRecordType.setLongitude("0.0000");
		usageRecordType.setPaymenttype("STORED_VALUE");
		usageRecordType.setFareset(Integer.parseInt(property.getProperty("fareset")));
		usageRecordType.setTTP(62);
		
		
		usageRecordType.setDateOfUsage(dateType);
		usageRecordType.setTimestamp(dateType);
		
		
		usageRecordType.setSequenceNumber(new BigInteger(1200+""));
		BigInteger sum = BigInteger.valueOf(0);
		sum = sum.add(BigInteger.valueOf(Long.parseLong(property.getProperty("sequencenumber"))));
		usageRecordType.setSequenceNumber(sum);
		usageRecordTypes.add(usageRecordType);
		deviceEventAPI.setRecords(new RecordsType());
		deviceEventAPI.getRecords().setUsages(new RecordsType.Usages());
		deviceEventAPI.getRecords().getUsages().getUsage().add(usageRecordType);
		
		String xml = makeXml(deviceEventAPI);
		post(uploadUrlString,awsAuthorizationKey,xml);
		
}


	private String post(String uploadUrlString, String awsAuthorizationKey, String xml) {
		HttpURLConnection con = null;
		//uploadUrlString ="https://cdta-intg.gfcp.io/services/device/authenticated/v2/event";
		String responseMessage = "";
		try {
			URL url = new URL(uploadUrlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");

			byte[] postDataBytes = null;
			int contentLength = 0;

			if (xml != null) {
				postDataBytes = xml.getBytes("UTF-8");
				contentLength = postDataBytes.length;
			}
			conn.setRequestProperty("Authorization", awsAuthorizationKey);
			conn.setRequestProperty("Content-Type", "application/xml");
			conn.setRequestProperty("Content-Length", String.valueOf(contentLength));
			conn.setDoOutput(true);
			conn.setConnectTimeout(20 * 1000);
			conn.setReadTimeout(60 * 1000);
			conn.connect();

			if (postDataBytes != null) {
				conn.getOutputStream().write(postDataBytes);
			}

			int responseCode = conn.getResponseCode();
			responseMessage = conn.getResponseMessage();
			log.info("Response Code: " + responseCode + ", Message: " + responseMessage);
		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			if (con != null) {
				con.disconnect();
			}
		}
		return responseMessage;
	}

	
	
	private String makeXml(DeviceEventAPI deviceEventAPI) {
		JAXBContext jaxbContext;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(DeviceEventAPI.class);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			sw = new StringWriter();
			jaxbMarshaller.marshal(deviceEventAPI, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		System.out.println(sw);
		return sw.toString();

	}
	
	
	public static XMLGregorianCalendar getXMLGregorianCalendar(String dateofusage){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try {
			date = format.parse(dateofusage);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);

		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	
}

