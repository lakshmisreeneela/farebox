package com.genfare.farebox.converter;

import com.genfare.cloud.device.record.DeviceEventAPI;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class DeviceEventAPIXMLConverter {
	@SuppressWarnings("restriction")
	private String makeXml(DeviceEventAPI deviceEventAPI) {
		JAXBContext jaxbContext;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(DeviceEventAPI.class);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			sw = new StringWriter();
			jaxbMarshaller.marshal(deviceEventAPI, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		System.out.println(sw);
		return sw.toString();

	}
}
