package com.genfare.farebox.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesRetrieve {
	
	static Properties prop = new Properties();

	public Properties getProperties() {
		try {
			 java.net.URL url = ClassLoader.getSystemResource("farebox.properties");
			 prop.load(url.openStream());
			
		} catch (IOException ex) {
			System.out.println("unable to load properties file");
			ex.printStackTrace();
		}
		
		
		return prop;
	}
}

