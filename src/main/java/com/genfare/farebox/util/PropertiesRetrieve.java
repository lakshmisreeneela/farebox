package com.genfare.farebox.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesRetrieve {
	
	static Properties prop = new Properties();

	public Properties getProperties() {
		InputStream input = null;
		try {
			
			 input = new FileInputStream("C:/farebox/device.properties");
			if (input != null) {
				prop.load(input);
			} 
		} catch (IOException ex) {

			ex.printStackTrace();
		}
		
	
		return prop;
	}
}

