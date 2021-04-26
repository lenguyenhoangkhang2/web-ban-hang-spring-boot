package com.congnghejava.webbanhang.payload.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupRequest {
	@NotBlank
	@Size(min = 6, max = 30)
	private String username;

	@NotBlank
	@Size(max = 50)
	private String email;

	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

	private Set<String> role;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRole() {
		return this.role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}

}
