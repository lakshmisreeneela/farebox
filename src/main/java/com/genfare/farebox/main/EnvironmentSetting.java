package com.genfare.farebox.main;

public class EnvironmentSetting {
	private static String fbSerialNumber = "002625" ;
	private static String fbPassword = "eb817670-ce4e-4193-bbe0-26c465f8e27d" ;
	private static String environment = "cdta-intg.gfcp.io";
	private static String tenant = "CDTA";
	 
	
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
		if (environment != null && environment.length() > 10) {
			environment = environment.trim();
			int lastIndex = environment.indexOf('-');
			if (lastIndex != -1) {
				String tenant = environment.substring(0, lastIndex);
				EnvironmentSetting.environment = environment;
				EnvironmentSetting.tenant = tenant.toUpperCase();

			} else {
				System.out.println("provide a valid environment followed by <tenant>-<environment>.gfcp.io");
			}
		} else {
			System.out.println("provide a valid environment followed by <tenant>-<environment>.gfcp.io");
		}

	}
	public static String getTenant() {
		return tenant;
	}
	public static void setTenant(String tenant) {
		EnvironmentSetting.tenant = tenant;
	}
	
	
	

}
