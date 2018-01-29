package com.genfare.farebox.main;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;

public class NamespaceResolver implements NamespaceContext {

	 private Document sourceDocument;
	 
	    public NamespaceResolver(Document document) {
	        sourceDocument = document;
	    }
	 
	    public String getNamespaceURI(String prefix) {
	    	switch(prefix)
	    	{
	    	case XMLConstants.DEFAULT_NS_PREFIX :return "http://genfare.com/cloud/device/header";
	    	case "ns2":return "http://genfare.com/cloud/device/autoload";
	    	case "ns3":return "http://genfare.com/cloud/device/common";
	    	default:return "";
	    	
	    	}
	    	 
	    }
	 
	    public String getPrefix(String namespaceURI) {
	    	
	        return sourceDocument.lookupPrefix(namespaceURI);
	    }
	 
	    @SuppressWarnings("rawtypes")
	    public Iterator getPrefixes(String namespaceURI) {
	        return null;
	    }

}
