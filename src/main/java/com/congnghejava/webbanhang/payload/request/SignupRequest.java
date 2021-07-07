package com.congnghejava.webbanhang.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupRequest {
	@NotBlank(message = "Vui lòng nhập tên của bạn")
	private String name;

	@Email(message = "Email không hợp lệ")
	@NotBlank(message = "Vui lòng nhập email")
	private String email;

	@Size(min = 8, max = 20, message = "Nhập mật khẩu từ 8 đến 20 ký tự")
	@NotBlank(message = "Vui lòng nhập mật khẩu")
	private String password;

	@NotBlank(message = "Vui lòng nhập mật khẩu xác nhận")
	private String confirmPassword;

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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
