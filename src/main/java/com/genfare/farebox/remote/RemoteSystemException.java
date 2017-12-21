package com.genfare.farebox.remote;

public class RemoteSystemException extends Exception {
private int responseCode = 0;
	
	
	public RemoteSystemException() {
		super();
	}
	

	public RemoteSystemException(String message) {
		super();
	}

	public RemoteSystemException(String message, int responseCode) {
		super(message);
		this.responseCode = responseCode;
		
	}

	public RemoteSystemException(Exception ex) {
		super(ex);
	}
	

	public RemoteSystemException(String message, Exception ex) {
		super(message, ex);
	}


	public RemoteSystemException(String message, int responseCode, Exception ex) {
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
