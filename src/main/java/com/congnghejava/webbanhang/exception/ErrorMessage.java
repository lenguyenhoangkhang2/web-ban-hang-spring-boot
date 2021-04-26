package com.congnghejava.webbanhang.exception;

import java.util.Arrays;
import java.util.List;

public class ErrorMessage {
	private int statusCode;
	private String message;
	private List<String> errors;

	public ErrorMessage(int statusCode, String message, List<String> errors) {
		this.statusCode = statusCode;
		this.message = message;
		this.errors = errors;
	}

	public ErrorMessage(int statusCode, String message, String error) {
		this.statusCode = statusCode;
		this.message = message;
		errors = Arrays.asList(error);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}
