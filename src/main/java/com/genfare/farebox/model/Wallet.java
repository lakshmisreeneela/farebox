package com.genfare.farebox.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Wallet")
public class Wallet {
String type;
String organization;
String farecode;
String status;
String nickname;
String registeredTo;
String attributes;

@XmlElement(name="identifiers")
List<Identifiers> identifiers;

@XmlElement(name="content")
List<WalletContents> content;

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public String getOrganization() {
	return organization;
}

public String getAttributes() {
	return attributes;
}

public void setAttributes(String attributes) {
	this.attributes = attributes;
}

public void setOrganization(String organization) {
	this.organization = organization;
}

public String getFarecode() {
	return farecode;
}

public void setFarecode(String farecode) {
	this.farecode = farecode;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public String getNickname() {
	return nickname;
}

public void setNickname(String nickname) {
	this.nickname = nickname;
}

public String getRegisteredTo() {
	return registeredTo;
}

public void setRegisteredTo(String registeredTo) {
	this.registeredTo = registeredTo;
}

public List<Identifiers> getIdentifiers() {
	return identifiers;
}

public void setIdentifiers(List<Identifiers> identifiers) {
	this.identifiers = identifiers;
}

public List<WalletContents> getContent() {
	return content;
}

public void setContent(List<WalletContents> content) {
	this.content = content;
}



}




