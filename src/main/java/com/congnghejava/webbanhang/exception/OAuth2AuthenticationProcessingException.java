package com.congnghejava.webbanhang.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	public OAuth2AuthenticationProcessingException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public OAuth2AuthenticationProcessingException(String msg) {
		super(msg);
	}
}
