package com.genfare.farebox.main;

import java.util.HashMap;
import java.util.Map;

public class DeviceAuthResponse {
	private AwsResponse Aws;

    private Map<String, URIResponse> configurations = new HashMap<String, URIResponse>();

    private Map<String, URIResponse> services = new HashMap<String, URIResponse>();

    private Map<String, URIResponse> software = new HashMap<String, URIResponse>();

    public AwsResponse getAws() {
        return Aws;
    }

    public void setAws(AwsResponse aws) {
        this.Aws = aws;
    }
    
    public Map<String, URIResponse> getConfigurations() {
    	return configurations;
    }

    public void setConfigurations(Map<String, URIResponse> configurations) {
    	this.configurations = configurations;
    } 

    public Map<String, URIResponse> getServices() {
        return services;
    }

    public void setServices(Map<String, URIResponse> services) {
        this.services = services;
    }

    public Map<String, URIResponse> getSoftware() {
        return software;
    }

    public void setSoftware(Map<String, URIResponse> software) {
        this.software = software;
    }

}
