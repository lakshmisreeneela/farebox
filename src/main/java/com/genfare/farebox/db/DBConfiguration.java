package com.genfare.farebox.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import com.genfare.farebox.main.FareBox;

public class DBConfiguration {
	private static final Logger log = Logger.getLogger(FareBox.class.getName());

	Properties property = new Properties();
	InputStream input = null;

	Connection con = null;

	public Connection getConnection() {

		try {
			String filename = "device.properties";
			input = FareBox.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				log.info("Sorry, unable to find " + filename);

			}
			property.load(input);
		}

		catch (IOException ex) {
			ex.printStackTrace();
		}

		String tenant = property.getProperty("tenant");

		try {
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + tenant, "vagrant", "vagrant");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return con;

	}

}
