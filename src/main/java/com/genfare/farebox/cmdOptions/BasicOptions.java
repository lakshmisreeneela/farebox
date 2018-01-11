package com.genfare.farebox.cmdOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import com.genfare.farebox.main.EnvironmentSetting;
import com.genfare.farebox.optionsImpl.DeviceAuthOptImpl;
import com.genfare.farebox.optionsImpl.RiderShipImpl;
import com.genfare.farebox.optionsImpl.UserLoginOptImpl;
import com.genfare.farebox.optionsImpl.WalletsOptImpl;

public class BasicOptions {
	private static final Logger log = Logger.getLogger(BasicOptions.class.getName());

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
		Option property2 = OptionBuilder.create("ridership");
		options.addOption(property2);
		options.addOption("help", false, "listing all options");
		options.addOption("devicelog", false, "listing Device configuration details");
		options.addOption("environment", false, "listing Device configuration details");
		options.addOption("exit", false, "exit from the farebox");
		options.addOption("authenticate", false, "authenticate farebox");
		OptionBuilder.withArgName("type value");
		OptionBuilder.hasArgs(2);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("start ridership");
		Option property3 = OptionBuilder.create("set");
		options.addOption(property3);

		OptionBuilder.withArgName("wallets username password");
		OptionBuilder.hasArgs(3);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("start ridership");
		Option property4 = OptionBuilder.create("get");
		options.addOption(property4);

		OptionBuilder.withArgName("username password");
		OptionBuilder.hasArgs(2);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("start ridership");
		Option property5 = OptionBuilder.create("login");
		options.addOption(property5);
		
		OptionBuilder.withArgName("electronicId cardNumber");
		OptionBuilder.hasArgs(2);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("start ridership");
		Option property6 = OptionBuilder.create("get");
		options.addOption(property6);
		
		for (;;) {
			try {
				System.out.println();
				System.out.print("farebox>");
				BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));

				String args2 = null;
				try {
					args2 = "-" + inp.readLine();
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
				String[] args3 = args2.split(" ");
				CommandLine line = new BasicParser().parse(options, args3);
				if (line.hasOption(args3[0])) {
					getResponse(args3[0], line);
				} else {
					System.out.println("Command not found");
				}

			} catch (Exception ex) {
				log.info(ex.getMessage());
				continue;
			}
		}

	}

	public static void getResponse(String option, CommandLine line) {
		String[] arguments;
		DeviceAuthOptImpl deviceAuth;
		switch (option) {
		case "-help":
			System.out.println("listing all options");
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
				case "electronicId":if (isValidate2(arguments))
				{
					
				}
			}
			break;

		case "-set":
			arguments = line.getOptionValues("set");
			if (isValidate2(arguments)) {
				switch (arguments[0]) {
				case "environment":
					EnvironmentSetting.setEnvironment(arguments[1]);
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

		case "-ridership":
			arguments = line.getOptionValues("ridership");
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
