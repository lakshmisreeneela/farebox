package com.genfare.farebox.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesRetrieve {
	//private static final Logger log = Logger.getLogger(PropertiesRetrieve.class.getName());
	static Properties prop = new Properties();

	public Properties getProperties() {
		try {
			prop.load(new FileInputStream("E://lakshmi/farebox.properties"));
							
		} catch (IOException ex) {

			ex.printStackTrace();
		}
	
		return prop;
	}
}
