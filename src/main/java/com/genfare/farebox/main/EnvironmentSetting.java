package com.genfare.farebox.main;

public class EnvironmentSetting {
	private static String fbSerialNumber = "002625" ;
	private static String fbPassword = "eb817670-ce4e-4193-bbe0-26c465f8e27d" ;
	private static String environment = "intg";
	
	public static String getFbSerialNumber() {
		return fbSerialNumber;
	}
	public static void setFbSerialNumber(String fbSerialNumber) {
		EnvironmentSetting.fbSerialNumber = fbSerialNumber;
	}
	public static String getFbPassword() {
		return fbPassword;
	}
	public static void setFbPassword(String fbPassword) {
		EnvironmentSetting.fbPassword = fbPassword;
	}
	public static String getEnvironment() {
		return environment;
	}
	public static void setEnvironment(String environment) {
		EnvironmentSetting.environment = environment;
	}
	
	
	

}
