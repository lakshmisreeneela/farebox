package com.genfare.farebox.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import com.genfare.cloud.device.record.AutoloadRecordType;
import com.genfare.cloud.device.record.DeviceEventAPI;
import com.genfare.cloud.device.record.RecordsType;
import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.farebox.clientrequest.DeviceAuthentication;
import com.genfare.farebox.clientrequest.RiderShip;

public class AWSConfig {

	public static void main(String[] args) {
		ClientConfiguration configuration = new ClientConfiguration();
		AmazonS3Client amazonS3 = new AmazonS3Client(new DefaultAWSCredentialsProviderChain(), configuration);
		S3Object s3object = amazonS3.getObject("intg-gfcp-device-configuration",
				"CDTA/1bf71427cd8b09bb36c44c78a4887e03/autoload.xml");
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
			Document doc = builder.parse(xml);

			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xPath = xpathFactory.newXPath();
			xPath.setNamespaceContext(new NamespaceResolver(doc));
			XPathExpression expr = xPath.compile("//ns2:AddProduct[ns2:ElectronicId='1156966386909056']");

			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			RiderShip riderShip = new RiderShip();
			DeviceEventAPI deviceEventAPI = riderShip.getDeviceHeader();

			deviceEventAPI = getAutoloadRecords(nodeList, deviceEventAPI);
			
			post(deviceEventAPI,"1156966386909056");
			
			
			
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	
	private static void post(DeviceEventAPI deviceEventAPI, String string) {
		DeviceAuthentication deviceAuthentication = new DeviceAuthentication();
		String serialNumber = EnvironmentSetting.getFbSerialNumber();
		String password = EnvironmentSetting.getFbPassword();
		DeviceAuthResponse deviceAuthResponse = deviceAuthentication.authenticate(serialNumber, password);
		if (deviceAuthResponse != null) {
			RiderShip uploadRecords = new RiderShip();
		
	}
	}



	private static DeviceEventAPI getAutoloadRecords(NodeList nodeList, DeviceEventAPI deviceEventAPI) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);
			NodeList childNodes = nNode.getChildNodes();

			AutoloadRecordType autoloadRecordType = new AutoloadRecordType();
			autoloadRecordType.setTerminalNumber(EnvironmentSetting.getFbSerialNumber());
			autoloadRecordType.setElectronicId("1156966386909056");

			BigInteger sum = BigInteger.valueOf(0);
			sum = sum.add(BigInteger.valueOf(Long.parseLong("3150151700592")));

			autoloadRecordType.setSequenceNumber(sum);
			autoloadRecordType.setStatus("SUCCESS");
			autoloadRecordType.setReasonCode("SUCCESS");
			
			deviceEventAPI.setRecords(new RecordsType());
			deviceEventAPI.getRecords().setAutoloads(new RecordsType.Autoloads());
			

			for (int j = 0; j < childNodes.getLength(); j++) {
				Node nNode2 = childNodes.item(j);

				System.out.println(nNode2.getNodeName() + ":" + nNode2.getTextContent());
				switch (nNode2.getNodeName()) {
				case "ns3:AutoloadType":
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
				}

			}
			
			deviceEventAPI.getRecords().getAutoloads().getAutoload().add(autoloadRecordType);
		}
		return deviceEventAPI;

	}
}
