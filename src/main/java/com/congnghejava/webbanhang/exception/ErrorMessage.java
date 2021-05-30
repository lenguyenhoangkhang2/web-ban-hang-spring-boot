package com.congnghejava.webbanhang.exception;

public class ErrorMessage {
	private int status;
	private String message;

	public ErrorMessage(int status, String message) {
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
