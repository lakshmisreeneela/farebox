package com.genfare.farebox.main;

public class EnvironmentSetting {
	private static String fbSerialNumber = "002625" ;
	private static String fbPassword = "eb817670-ce4e-4193-bbe0-26c465f8e27d" ;
	private static String environment = "cdta-intg.gfcp.io";
	private static String tenant = "CDTA";
	private static String env = "intg";
	private static String dateofusage;

	
	
	
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
		environment = tenant.toLowerCase()+"-"+env+".gfcp.io";
		return environment;
	}

	
	public static String getTenant() {
		return tenant;
	}

	public static void setTenant(String tenant) {
		EnvironmentSetting.tenant = tenant.toUpperCase();
	}
	public static String getEnv() {
		return env;
	}
	public static String getDateofusage() {
		return dateofusage;
	}
	public static void setDateofusage(String dateofusage) {
		EnvironmentSetting.dateofusage = dateofusage;
	}
	public static void setEnvironment(String environment) {
		EnvironmentSetting.environment = environment;
	}
	public static void setEnv(String env) {
		EnvironmentSetting.env = env.toLowerCase();
	}
	
	
	

}
