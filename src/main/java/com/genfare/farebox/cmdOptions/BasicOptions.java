package com.genfare.farebox.cmdOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.genfare.farebox.main.EnvironmentSetting;
import com.genfare.farebox.optionsImpl.DeviceAuth;

public class BasicOptions {
	
	public static void main(String[] args) throws ParseException
	{
	
	
	Options options = new Options();
	
	OptionBuilder.withArgName("serialNumber,password");
	OptionBuilder
            .hasArgs(2);
	OptionBuilder
            .withValueSeparator(',');
	OptionBuilder
            .withDescription("start ridership");
	Option property  = OptionBuilder
            .create("auth");
	options.addOption(property);
	
	
	OptionBuilder.withArgName("electronicId,sequencenumber");
	OptionBuilder
            .hasArgs(2);
	OptionBuilder
            .withValueSeparator(',');
	OptionBuilder
            .withDescription("start ridership");
	Option property2  = OptionBuilder
            .create( "ridership");
	options.addOption(property2);
	
	for(; ;)
	{
	System.out.print("farebox>");
	BufferedReader inp = new BufferedReader (new InputStreamReader(System.in));	
	
	String args2 = null;
	try {
		args2 = inp.readLine();
	} catch (IOException e) {
		e.printStackTrace();
	}
	String[] args3 = args2.split(" ");
	CommandLine line = new BasicParser().parse(options, args3);
	switch(args3[0])
	{
	case "help" :System.out.println("listing all options");
				break;
				
	case "auth" :if(args3.length<3){
		System.out.println("must have two arguments");
		}
	
					DeviceAuth deviceAuth = new DeviceAuth();
					String serialNumber = args3[1];
					String assetPassword = args3[2];
					System.out.println(deviceAuth.authenticate(serialNumber,assetPassword));
					break;
					
	case "set"  :if(args3.length<3)
			{
		System.out.println("must have two arguments");	
			}
	switch(args3[1])
	{
	case "environment":	EnvironmentSetting.setEnvironment(args3[2]);
		break;
	case "fbSerialNumber":EnvironmentSetting.setFbSerialNumber(args[3]);
		break;
	case "fbPassword":EnvironmentSetting.setEnvironment(args[2]);
		break;
	}
	
	
	}
	
}
}
}
