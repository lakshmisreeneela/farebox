package com.genfare.farebox.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import com.genfare.farebox.clientrequest.DeviceAuthentication;

public class PropertiesRetrieve {
	private static final Logger log = Logger.getLogger(PropertiesRetrieve.class.getName());
	static Properties prop = new Properties();

	public Properties getProperties() {

		try {
			String filename = "device.properties";
			InputStream input = DeviceAuthentication.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				log.info("Sorry, unable to find " + filename);

			}
			prop.load(input);
		} catch (IOException ex) {

			ex.printStackTrace();
		}
		return prop;
	}
}
