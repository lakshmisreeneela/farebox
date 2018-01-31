package com.genfare.farebox.clientrequest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.jboss.resteasy.util.Base64;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;

import com.genfare.cloud.device.common.DateType;
import com.genfare.cloud.device.record.AutoloadRecordType;
import com.genfare.cloud.device.record.DeviceEventAPI;
import com.genfare.cloud.device.record.RecordsType;
import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.cloud.osgi.device.auth.response.AwsResponse.AwsCredentials;
import com.genfare.farebox.main.EnvironmentSetting;
import com.genfare.farebox.main.NamespaceResolver;
import com.genfare.farebox.util.PropertiesRetrieve;

public class AutoloadProcess {

	static final String AUTH_HEADER_PROPERTY = "Authorization";
	InputStream input = null;
	
	PropertiesRetrieve propertiesRetrieve = new PropertiesRetrieve();
	Properties property = propertiesRetrieve.getProperties(); 
	DateType dateType = new DateType();
	
	public String uploadRecords(DeviceAuthResponse deviceAuthResponse,String electronicId, String sequenceNumber) {

		AwsCredentials awsCredentials = deviceAuthResponse.getAws().getCredentials();
		String accessKey = awsCredentials.getAccessKey();
		String secretKey = awsCredentials.getSecretKey();
		String sessionId = awsCredentials.getSessionId();
		byte[] authorizationBytes = (accessKey + " | " + secretKey + " | " + sessionId).getBytes();
		String awsAuthorizationKey = new String(Base64.encodeBytes(authorizationBytes));
		String uploadUrlString = "https://"+EnvironmentSetting.getEnvironment()+"/services/device/authenticated/v2/event";
		RiderShip riderShip = new RiderShip();
		DeviceEventAPI deviceEventAPI = riderShip.getDeviceHeader();
		deviceEventAPI = getAutoloadXML(electronicId,deviceEventAPI,sequenceNumber);
		
		String xml = riderShip.makeXml(deviceEventAPI);
		return riderShip.post(uploadUrlString, awsAuthorizationKey, xml);
		//return "";

	}


	public DeviceEventAPI  getAutoloadXML(String electronicId, DeviceEventAPI deviceEventAPI, String sequenceNumber)
	{
		ClientConfiguration configuration = new ClientConfiguration();
		AmazonS3Client amazonS3 = new AmazonS3Client(new DefaultAWSCredentialsProviderChain(), configuration);
		S3Object s3object = amazonS3.getObject(EnvironmentSetting.getEnv()+"-gfcp-device-configuration",EnvironmentSetting.getTenant()+"/1bf71427cd8b09bb36c44c78a4887e03/autoload.xml");
		BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
		String line;
		StringBuilder autoloadxml = new StringBuilder();
		
		try {

			while ((line = reader.readLine()) != null) {
				autoloadxml = autoloadxml.append(line);
			}

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource xml = new InputSource(new StringReader(autoloadxml.toString()));
			
			System.out.println(autoloadxml.toString());
			Document doc = builder.parse(xml);

			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xPath = xpathFactory.newXPath();
			xPath.setNamespaceContext(new NamespaceResolver(doc));
			XPathExpression addValue = xPath.compile("//ns2:AddValue[ns2:ElectronicId='"+electronicId+"']");
			
			NodeList nodeListWTAddValues = (NodeList) addValue.evaluate(doc, XPathConstants.NODESET);
			deviceEventAPI = getAutoloadRecords(nodeListWTAddValues,deviceEventAPI,electronicId,sequenceNumber);
			XPathExpression addProducts = xPath.compile("//ns2:AddProducts[ns2:ElectronicId='"+electronicId+"']");
			NodeList nodeListWTAddProducts = (NodeList) addProducts.evaluate(doc, XPathConstants.NODESET);
			deviceEventAPI = getAutoloadRecords(nodeListWTAddProducts,deviceEventAPI,electronicId,sequenceNumber);
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	
	return deviceEventAPI;
	
}
	
	
	
	private static DeviceEventAPI getAutoloadRecords(NodeList nodeList, DeviceEventAPI deviceEventAPI,String electronicId,String sequenceNumber) {
		Long seqNumber = Long.parseLong(sequenceNumber);
		
		System.out.println(nodeList.getLength());
		DateType dateType = new DateType();
		XMLGregorianCalendar xMLGregorianCalendar = null;
		if(EnvironmentSetting.getDateofusage() == null)
		{
			Date date=new Date(); 
			 xMLGregorianCalendar = RiderShip.getXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
		
	    }else
	     {
	    	 xMLGregorianCalendar = RiderShip.getXMLGregorianCalendar(EnvironmentSetting.getDateofusage());
	     }
		List<AutoloadRecordType> autoloadRecordTypeList = new ArrayList<AutoloadRecordType>();
		dateType.setLocalTime(false);
		dateType.setValue(xMLGregorianCalendar);
		Byte pendingCount = 0;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);
			NodeList childNodes = nNode.getChildNodes();
			
		
			AutoloadRecordType autoloadRecordType = new AutoloadRecordType();
			autoloadRecordType.setTerminalNumber(EnvironmentSetting.getFbSerialNumber());
			autoloadRecordType.setElectronicId(electronicId);
			autoloadRecordType.setTimestamp(dateType);
			BigInteger sum = BigInteger.valueOf(0);
			sum = sum.add(BigInteger.valueOf(seqNumber));
			seqNumber++;

			autoloadRecordType.setSequenceNumber(sum);
			autoloadRecordType.setStatus("SUCCESS");
			autoloadRecordType.setReasonCode("SUCCESS");
			autoloadRecordType.setTimestamp(dateType);
			autoloadRecordType.setPendingCount(pendingCount);
			pendingCount++;
			deviceEventAPI.setRecords(new RecordsType());
			deviceEventAPI.getRecords().setAutoloads(new RecordsType.Autoloads());
			
			
			NodeList ticketChildNodes = childNodes.item(8).getChildNodes();
			autoloadRecordType.setTicketId(Integer.parseInt(ticketChildNodes.item(0).getTextContent()));
			

			for (int j = 0; j < childNodes.getLength(); j++) {
				Node nNode2 = childNodes.item(j);

				System.out.println(nNode2.getNodeName() + ":" + nNode2.getTextContent());
				switch (nNode2.getNodeName()) {
				case "ns2:AutoloadType":
					autoloadRecordType.setAutoloadType(nNode2.getTextContent());
					break;
				case "ns2:TicketId":
					autoloadRecordType.setTicketId(Integer.parseInt(nNode2.getTextContent()));
					break;
				case "ns2:OrderId":
					autoloadRecordType.setOrderId(Integer.parseInt(nNode2.getTextContent()));
					break;
				case "ns2:OrderItemNumber":
					autoloadRecordType.setOrderItemNumber(Integer.parseInt(nNode2.getTextContent()));
					break;
				case "ns2:Slot":
					autoloadRecordType.setSlot(Byte.parseByte(nNode2.getTextContent()));
					break;
				case "ns2:ValueActivity":
					autoloadRecordType.setValueActivity(new BigDecimal(nNode2.getTextContent()));
					break;

				case "ns2:LoadSequence":
					autoloadRecordType.setLoadSequence(Byte.parseByte(nNode2.getTextContent()));
					break;
				default :
				}

			}
			autoloadRecordTypeList.add(autoloadRecordType);
		}
			
			deviceEventAPI.getRecords().getAutoloads().getAutoload().addAll(autoloadRecordTypeList);
		return deviceEventAPI;

	}
	
	
}
