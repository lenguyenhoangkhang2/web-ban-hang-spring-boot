package com.congnghejava.webbanhang.payload.response;

public class AuthResponse {
	private String accsessToken;
	private String tokenType = "Bearer";

	public AuthResponse(String accessToken) {
		this.accsessToken = accessToken;
	}

	public String getAccsessToken() {
		return accsessToken;
	}

	public void setAccsessToken(String accsessToken) {
		this.accsessToken = accsessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
}
