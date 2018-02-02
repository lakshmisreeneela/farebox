package com.genfare.farebox.util;

public class ListOptions {

	public void getListOfOptions()
	{
		System.out.println("For more information on a specific command, type HELP command-name");
		System.out.println("AUTHENTICATE Authenticate the existing device details.");
		System.out.println("AUTH         Authenticate and sets new device datails.");
		System.out.println("ENVIRONMENT  Displays the environment deatils.");
		System.out.println("DEVICELOG    Dispalys the device dtails.");
		System.out.println("SET          Sets Device and Environment variables.");
		System.out.println("GET          Gives the required data based on option and argument.");
		System.out.println("TAP          Perform ridership event for the specified card.");
		System.out.println("AUTOLOAD    Autoload the processing productes into the card");
	}
	
	
	public void getCommandDescription(String command)
	{
		switch(command)
		{
		case "authenticate":System.out.println("no need to provide any arguments simply use authenticate");
		break;
		case "environment":System.out.println("no need to provide any arguments simply use environment");
		break;
		case "auth":System.out.println("need to provide two arguments device serialNumber and Password");
		System.out.println("syntax: "+"auth <DeviceSerialNumber> <DevicePassword>");
		break;
		case "set":System.out.println("syntax: "+"set env <TenantName> <Environment>");
		break;
		case "get":System.out.println("syntax: "+"get eid <cardNumber> (gives electronic_Id of the card)");
		break;
		case "tap":System.out.println("need to provide two arguments cardNumber of the card and SequenceNumber");
					System.out.println("syntax: "+"tap <cardNumber> <sequenceNumber>");
		case "autoload":System.out.println("need to provide two arguments cardNumber of the card and SequenceNumber");
		System.out.println("syntax: "+"autoload <cardNumber> <sequenceNumber>");
		break;
		
		}
	}
	
}
