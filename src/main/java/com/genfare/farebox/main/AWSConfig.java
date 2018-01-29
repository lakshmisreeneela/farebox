package com.genfare.farebox.main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.genfare.cloud.device.autoload.AutoloadAPIType;
import com.genfare.cloud.device.record.AutoloadRecordType;
import com.genfare.cloud.device.record.DeviceEventAPI;
import com.genfare.farebox.clientrequest.RiderShip;

public class AWSConfig {
	
	public static void main(String[] args)
	{
	ClientConfiguration configuration = new ClientConfiguration();
	AmazonS3Client amazonS3 = new AmazonS3Client(new DefaultAWSCredentialsProviderChain(), configuration);
	S3Object s3object = amazonS3.getObject("intg-gfcp-device-configuration","CDTA/1bf71427cd8b09bb36c44c78a4887e03/autoload.xml");
	BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
	String line;
	StringBuilder autoloadxml = new StringBuilder();
	
	try {
		
		while((line = reader.readLine()) != null) {
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
         String s = (String) expr.evaluate(doc, XPathConstants.STRING);
         

        
         for (int i = 0; i < nodeList.getLength(); i++) {
        	 Node nNode = nodeList.item(i);
        	 NodeList childNodes = nNode.getChildNodes();
        	 
        	 for(int j=0;j<childNodes.getLength();j++)
        	 	{
        		 Node nNode2 = childNodes.item(j);
        		 
    			 System.out.println(nNode2.getNodeName() +":"+nNode2.getTextContent());
    			 
        	 	}
        	 }

         System.out.println(s);
         
            
		
	
		
		
		
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(AutoloadAPIType.class);
			InputStream is = new ByteArrayInputStream(autoloadxml.toString().getBytes());
			Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();
			AutoloadAPIType autoloadAPIType = new AutoloadAPIType();
			autoloadAPIType = (AutoloadAPIType) jaxbMarshaller.unmarshal(is);
			System.out.println(autoloadAPIType.toString());
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	
		
		
		
		
		
		
	} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} 
	
	
}
}
