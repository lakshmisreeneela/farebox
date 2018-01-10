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
import org.apache.commons.cli.ParseException;

import com.genfare.farebox.main.EnvironmentSetting;
import com.genfare.farebox.optionsImpl.DeviceAuth;
import com.genfare.farebox.optionsImpl.RiderShip;

public class BasicOptions {
	private static final Logger log = Logger.getLogger(BasicOptions.class.getName());

	public static void main(String[] args) throws ParseException {
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
		options.addOption("environmentlog", false, "listing Device configuration details");
		options.addOption("exit", false, "exit from the farebox");
		options.addOption("authenticate", false, "authenticate farebox");
		OptionBuilder.withArgName("type value");
		OptionBuilder.hasArgs(2);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("start ridership");
		Option property3 = OptionBuilder.create("set");
		options.addOption(property3);
		
		OptionBuilder.withArgName("get wallets username password");
		OptionBuilder.hasArgs(4);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("start ridership");
		Option property4 = OptionBuilder.create("set");
		options.addOption(property4);
		
		for (;;) {
			try {
				System.out.print("farebox>");
				BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));

				String args2 = null;
				try {
					args2 ="-"+inp.readLine();
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
		DeviceAuth deviceAuth;
		switch (option) {
		case "-help":
			System.out.println("listing all options");
			break;
		case "-authenticate":deviceAuth = new DeviceAuth();
			System.out.println(deviceAuth.authenticate(EnvironmentSetting.getFbSerialNumber(),EnvironmentSetting.getFbPassword()));
			break;
		case "-auth":
			deviceAuth = new DeviceAuth();
			arguments = line.getOptionValues("auth");
			if(isValidate(arguments))
			System.out.println(deviceAuth.authenticate(arguments[0], arguments[1]));
			else
				System.out.println("must have two arguments(SerialNumber and Password");
			break;
		case "-get":arguments = line.getOptionValues("get");

			if(isValidate(arguments))
			{
				
			}

		case "-set":
			arguments = line.getOptionValues("set");
			if(isValidate(arguments))
			{
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
			default :System.out.println("there is no such variable");

				break;
			}
			}
			else
			{
				System.out.println("must have two arguments(Typel and VALUE)");
			}
			break;
			
		case "-ridership":
			arguments = line.getOptionValues("ridership");
			if (isValidate(arguments)) {
				RiderShip riderShip = new RiderShip();
				riderShip.riderShipProcess(arguments[0], arguments[1]);
				
			} else {
				System.out.println("must have two arguments(ElectronicId and SequenceNumber)");
			}
		case "-devicelog":
			System.out.println("serialNumber :" + EnvironmentSetting.getFbSerialNumber());
			System.out.println("assetPassword :" + EnvironmentSetting.getFbPassword());
			break;
		case "-environmentlog":
			System.out.println("Environment :" + EnvironmentSetting.getEnvironment());
			break;
		case "-exit":return ;

		}
		
	}
	
	
	public static boolean isValidate(String[] args)
	{
		if(args.length<2)
		{
			return false;
		}
		return true;
	}
}
