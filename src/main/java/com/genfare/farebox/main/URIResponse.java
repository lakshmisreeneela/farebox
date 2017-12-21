package com.genfare.farebox.main;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
//@JsonInclude(Include.NON_NULL)
public class URIResponse {
	private String uri;

    private String method;

    private String s3key;

    private String version;

    public URIResponse() {

    }

    public URIResponse(String uri, String method) {
        this.uri = uri;
        this.method = method;
    }

    public URIResponse(String uri, String method, String s3key) {
        this(uri, method);
        this.s3key = s3key;
    }

    public URIResponse(String uri, String method, String s3key, String version) {
        this(uri, method, s3key);
        this.version = version;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getS3key() {
        return s3key;
    }

    public void setS3key(String s3key) {
        this.s3key = s3key;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
