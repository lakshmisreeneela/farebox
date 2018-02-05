package com.genfare.farebox.util;

import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.genfare.cloud.device.common.DateType;
import com.genfare.cloud.device.header.DeviceHeaderType;
import com.genfare.cloud.device.record.DeviceEventAPI;
import com.genfare.farebox.main.EnvironmentSetting;

public class Usage {
	
	private static final Logger log = Logger.getLogger(Usage.class.getName());
	

	PropertiesRetrieve propertiesRetrieve = new PropertiesRetrieve();
	Properties property = propertiesRetrieve.getProperties(); 
	String tenant=EnvironmentSetting.getTenant().toLowerCase();
	DateType dateType = new DateType();
	String fbxNo = property.getProperty(tenant+"."+EnvironmentSetting.getEnv()+".fbxno");
	

	public DeviceEventAPI getDeviceHeader() {

		DeviceEventAPI deviceEventAPI = new DeviceEventAPI();
		DeviceHeaderType deviceHeaderType = new DeviceHeaderType();
		dateType.setLocalTime(false);
		XMLGregorianCalendar xMLGregorianCalendar = null;

		if (EnvironmentSetting.getDateofusage() == null) {
			Date date = new Date();
			xMLGregorianCalendar = this
					.getXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

		} else {
			xMLGregorianCalendar = this.getXMLGregorianCalendar(EnvironmentSetting.getDateofusage());
		}

		dateType.setValue(xMLGregorianCalendar);
		deviceHeaderType.setTenantName(EnvironmentSetting.getTenant());
		deviceHeaderType.setEnvironment(EnvironmentSetting.getEnv());
		deviceHeaderType.setDeviceType(property.getProperty("deviceType"));
		deviceHeaderType.setOrganization(EnvironmentSetting.getTenant());
		deviceHeaderType.setSourceId(fbxNo);
		deviceHeaderType.setAction("UPDATE");
		deviceHeaderType.setTestMode(true);
		deviceHeaderType.setRetryNumber(1);
		deviceHeaderType.setDateSent(dateType);
		deviceHeaderType.setLocation(property.getProperty("Location"));
		deviceHeaderType.setMessageId(property.getProperty("MessageId"));
		deviceHeaderType.setCorrelationId(property.getProperty("CorrelationId"));
		deviceEventAPI.setHeader(deviceHeaderType);
		return deviceEventAPI;

	}
	

	public XMLGregorianCalendar getXMLGregorianCalendar(String dateofusage) {
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



	public String makeXml(DeviceEventAPI deviceEventAPI) {
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


	public String post(String uploadUrlString, String awsAuthorizationKey, String xml) {
		HttpURLConnection con = null;
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
			if (responseCode == 200) {
				System.out.println("process completed successfully");
			} else {
				log.info("sorry, unable to process");
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return responseMessage;
	}

}
