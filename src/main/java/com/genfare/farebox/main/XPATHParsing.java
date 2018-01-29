package com.genfare.farebox.main;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class XPATHParsing {
	public static void main(String[] args)
	{
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse("/farebox/sample.xml");

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            XPathExpression expr = xpath.compile("//user[emailid='lakshmi@gmail.com'][last()]");
                String s = (String) expr.evaluate(doc, XPathConstants.STRING);
                System.out.println(s);
//                for(int i=0;i<nodes.getLength();i++)
//                {
//                	NodeList nodeList = nodes.item(i).getChildNodes();
//                	for(int j=0;i<nodes.getLength();j++)
//                	System.out.println(nodeList.item(j).getTextContent()+""+nodeList.item(j).getNodeName());
//                }
//              

          }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
}
