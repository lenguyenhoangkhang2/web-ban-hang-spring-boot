package com.congnghejava.webbanhang.payload.response;

import java.util.List;
import java.util.stream.Collectors;

import com.congnghejava.webbanhang.models.UserCredential;

public class UserInfoResponse {
	private String email;
	private boolean emailVerified;
	private List<String> authorities;
	private String name;
	private String avatarUrl;

	public UserInfoResponse(UserCredential userCredential) {
		this.email = userCredential.getEmail();
		this.emailVerified = userCredential.getEmailVerified();
		this.authorities = userCredential.getRoles().stream().map(role -> role.getName().name())
				.collect(Collectors.toList());
		this.name = userCredential.getUser().getName();
		this.avatarUrl = userCredential.getUser().getAvatarUrl();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

}
