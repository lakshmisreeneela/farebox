package com.genfare.farebox.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="content")
public class WalletContents {

private String identifier;

private String ticketIdentifier;

private String description;

private String type;

private String balance;

private String uri;

public String getIdentifier() {
	return identifier;
}

public void setIdentifier(String identifier) {
	this.identifier = identifier;
}

public String getTicketIdentifier() {
	return ticketIdentifier;
}

public void setTicketIdentifier(String ticketIdentifier) {
	this.ticketIdentifier = ticketIdentifier;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public String getBalance() {
	return balance;
}

public void setBalance(String balance) {
	this.balance = balance;
}

public String getUri() {
	return uri;
}

public void setUri(String uri) {
	this.uri = uri;
}



}
