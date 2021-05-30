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

	private String phone;

	private String country;

	private String province;

	private String district;

	private String detail;

	public UserInfoResponse(UserCredential userCredential) {
		this.email = userCredential.getEmail();
		this.emailVerified = userCredential.getEmailVerified();
		this.authorities = userCredential.getRoles().stream().map(role -> role.getName().name())
				.collect(Collectors.toList());
		this.name = userCredential.getUser().getName();
		this.avatarUrl = userCredential.getUser().getAvatarUrl();
		this.phone = userCredential.getUser().getUserInfo().getPhone();
		this.country = userCredential.getUser().getUserInfo().getCountry();
		this.province = userCredential.getUser().getUserInfo().getProvince();
		this.district = userCredential.getUser().getUserInfo().getDistrict();
		this.detail = userCredential.getUser().getUserInfo().getDetail();
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
