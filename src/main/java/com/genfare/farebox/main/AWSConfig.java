package com.genfare.farebox.main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;

public class AWSConfig {
	public static void main(String[] args)
	{
	  ClientConfiguration configuration = new ClientConfiguration();
	  AmazonS3Client amazonS3 = new AmazonS3Client(new DefaultAWSCredentialsProviderChain(), configuration);
	System.out.println(amazonS3.getServiceName());
	System.out.println(new DefaultAWSCredentialsProviderChain().getCredentials().getAWSAccessKeyId());
	System.out.println(new DefaultAWSCredentialsProviderChain().getCredentials().getAWSSecretKey());
//	ObjectListing ol = amazonS3.listObjects("intg-gfcp-device-configuration");
//	
//	List<S3ObjectSummary> objects = ol.getObjectSummaries();
//	for (S3ObjectSummary os: objects) {
//	    System.out.println("* " + os.getKey());
//	}
	
	S3Object s3object = amazonS3.getObject("intg-gfcp-device-configuration","CDTA/1bf71427cd8b09bb36c44c78a4887e03/autoload.xml");
	BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
	String line;
	String autoloadxml = null;
	try {
		
		while((line = reader.readLine()) != null) {
			autoloadxml = autoloadxml+line;
			}
		
		System.out.println(autoloadxml);
		autoloadxml = autoloadxml.replace("null","");
		DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		ByteArrayInputStream input =  new ByteArrayInputStream(autoloadxml.getBytes("UTF-8"));
		Document doc = builder.parse(input);
		doc.getDocumentElement().normalize();
        
		XPath xPath =  XPathFactory.newInstance().newXPath();
		String expression = "ns2:Autoloads/ns2:AddProducts/ns2:AddProduct";	        
		NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
		
		if(nodeList != null)
		{
			System.out.println(nodeList.getClass());
		}
		for (int i = 0; i < nodeList.getLength(); i++) {
			   Node nNode = nodeList.item(i);
		
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
