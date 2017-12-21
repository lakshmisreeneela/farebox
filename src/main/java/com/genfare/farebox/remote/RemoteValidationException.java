package com.genfare.farebox.remote;

public class RemoteValidationException  extends Exception {
private int responseCode = 0;
	
	
	public RemoteValidationException() {
		super();
	}
	

	public RemoteValidationException(String message) {
		super(message);
	}

	public RemoteValidationException(String message, int responseCode) {
		super(message);
		this.responseCode = responseCode;
	}

	public RemoteValidationException(Exception ex) {
		super(ex);
	}
	

	public RemoteValidationException(String message, Exception ex) {
		super(message, ex);
	}


	public RemoteValidationException(String message, int responseCode, Exception ex) {
		super(message, ex);
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}


	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
	
}
