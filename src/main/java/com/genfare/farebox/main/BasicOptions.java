package com.genfare.farebox.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import com.genfare.farebox.main.EnvironmentSetting;
import com.genfare.farebox.optionsImpl.DeviceAuthOptImpl;
import com.genfare.farebox.optionsImpl.ElectronicID;
import com.genfare.farebox.optionsImpl.RiderShipImpl;
import com.genfare.farebox.optionsImpl.UserLoginOptImpl;
import com.genfare.farebox.optionsImpl.WalletsOptImpl;
import com.genfare.farebox.util.ListOptions;

public class BasicOptions {
	private static final Logger log = Logger.getLogger(BasicOptions.class.getName());
	public static  ArrayList<String> commands = new ArrayList<String>();
	public static void main(String[] args) {
		Options options = new Options();

		OptionBuilder.withArgName("serialNumber password");
		OptionBuilder.hasArgs(2);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("start ridership");
		Option property = OptionBuilder.create("auth");

		options.addOption(property);

		OptionBuilder.withArgName("electronicId sequencenumber");
		OptionBuilder.hasArgs(2);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("start ridership");
		Option property2 = OptionBuilder.create("tap");
		options.addOption(property2);
		options.addOption("list","LIST", false, "listing all options");
		options.addOption("devicelog", false, "listing Device configuration details");
		options.addOption("environment", false, "listing Device configuration details");
		options.addOption("exit", false, "exit from the farebox");
		options.addOption("authenticate", false, "authenticate farebox");
		options.addOption("help",true, "fdgfdghfgh");
		OptionBuilder.withArgName("type value");
		OptionBuilder.hasArgs(2);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("start ridership");
		Option property3 = OptionBuilder.create("set");
		options.addOption(property3);

		OptionBuilder.withArgName("wallets username password");
		OptionBuilder.hasArgs(3);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("retrieving wallets");
		Option property4 = OptionBuilder.create("get");
		options.addOption(property4);

		OptionBuilder.withArgName("username password");
		OptionBuilder.hasArgs(2);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("user login");
		Option property5 = OptionBuilder.create("login");
		options.addOption(property5);
		
		OptionBuilder.withArgName("eid cardNumber");
		OptionBuilder.hasArgs(2);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("get electronic_Id");
		Option property6 = OptionBuilder.create("get");
		options.addOption(property6);
		System.out.println("Usage : <command> <option> <arguments..>");
		System.out.println("example list");
		for (;;) {
			try {
				System.out.println();
				if(args.length==0)
				System.out.printf("farebox>");
				else
				{
					System.out.printf(args[0]+">");	
				}
				
				BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));

				String args2 = null;
				try {
					args2 = inp.readLine();
					commands.add(args2);
					args2 = "-" + args2;
				} catch (IOException e) {
					
					e.printStackTrace();
					continue;
				}
				String[] args3 = args2.split(" ");
				CommandLine line = new BasicParser().parse(options, args3);
				String command = args3[0].toLowerCase();
				if (line.hasOption(command)) {
					getResponse(command, line);
				} else {
					System.out.println("Command not found");
				}

			} catch (Exception ex) {
				System.out.println("Error:"+ex.getMessage());
				continue;
			}
		}

	}

	
	public static void getResponse(String option, CommandLine line) {
		String[] arguments;
		DeviceAuthOptImpl deviceAuth;
		switch (option) {
		case "-list":ListOptions listOptions = new ListOptions();
		listOptions.getListOfOptions();
			break;
		case "-authenticate":
			deviceAuth = new DeviceAuthOptImpl();
			System.out.println(deviceAuth.authenticate(EnvironmentSetting.getFbSerialNumber(),
					EnvironmentSetting.getFbPassword()));
			break;
		case "-auth":
			deviceAuth = new DeviceAuthOptImpl();
			arguments = line.getOptionValues("auth");
			if (isValidate2(arguments))
				System.out.println(deviceAuth.authenticate(arguments[0], arguments[1]));
			else
				System.out.println("must have two arguments(SerialNumber and Password");
			break;
		case "-get":
			arguments = line.getOptionValues("get");
				switch (arguments[0]) {
				case "wallets":if (isValidate3(arguments))
				{
					WalletsOptImpl wallets = new WalletsOptImpl();

					wallets.getWallets(arguments[1], arguments[2]);
				}
				else
				{
					System.out.println("provide Username and Password");
				}
					break;
				case "eid":if (isValidate2(arguments))
				{
					ElectronicID electronicID = new ElectronicID();
					electronicID.getElectronicId(arguments[1]);
					
				}
				else
				{
					System.out.println("must have a option and one argument");
				}
			}
			break;

		case "-set":
			arguments = line.getOptionValues("set");
			if (isValidate2(arguments)) {
				switch (arguments[0]) {
				case "env":
					EnvironmentSetting.setEnv(arguments[1]);
					break;
				case "tenant":
					EnvironmentSetting.setTenant(arguments[1]);
					break;	
				case "fbxSerialNumber":
					EnvironmentSetting.setFbSerialNumber(arguments[1]);
					break;
				case "fbxPassword":
					EnvironmentSetting.setFbPassword(arguments[1]);
					break;
				default:
					System.out.println("there is no such variable");

					break;
				}
			} else {
				System.out.println("must have two arguments(Typel and VALUE)");
			}
			break;

		case "-tap":
			arguments = line.getOptionValues("tap");
			if (isValidate2(arguments)) {
				RiderShipImpl riderShip = new RiderShipImpl();
				riderShip.riderShipProcess(arguments[0], arguments[1]);

			} else {
				System.out.println("must have two arguments(ElectronicId and SequenceNumber)");
			}
			break;
		case "-devicelog":
			System.out.println("serialNumber :" + EnvironmentSetting.getFbSerialNumber());
			System.out.println("assetPassword :" + EnvironmentSetting.getFbPassword());
			
			break;
		case "-login":
			arguments = line.getOptionValues("login");
			if (isValidate2(arguments)) {
				UserLoginOptImpl userLoginOptImpl = new UserLoginOptImpl();
				userLoginOptImpl.userLogin(arguments[0], arguments[1]);
			} else {
				System.out.println("must have two arguments(username and password)");
			}
			break;
		case "-environment":
			System.out.println("Environment :" + EnvironmentSetting.getEnvironment());
			System.out.println("tenant :" +EnvironmentSetting.getTenant());
			System.out.println("env :" + EnvironmentSetting.getEnv());
			break;
		
		case "-help":arguments = line.getOptionValues("help");
					if(arguments.length==1)
					{
						ListOptions listOptions1 = new ListOptions();
						listOptions1.getCommandDescription(arguments[0]);
					}
			break;
		
		case "-exit":
			System.exit(0);

		}

	}

	private static boolean isValidate3(String[] arguments) {
		if (arguments.length < 3) {
			return false;
		}
		return true;
	}

	public static boolean isValidate2(String[] arguments) {
		if (arguments.length < 2) {
			return false;
		}
		return true;
	}
}
