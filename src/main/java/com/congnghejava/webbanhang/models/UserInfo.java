package com.congnghejava.webbanhang.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_info")
public class UserInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String phone;

	private String country;

	private String province;

	private String district;

	private String detail;

	@OneToOne(mappedBy = "userInfo")
	private User user;

	public UserInfo() {
		this.phone = "";
		this.country = "";
		this.province = "";
		this.district = "";
		this.detail = "";
	}

	public UserInfo(String phone, String country, String province, String district, String detail) {
		this.phone = phone;
		this.country = country;
		this.province = province;
		this.district = district;
		this.detail = detail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isFullfill() {
		return !(phone.isEmpty() || country.isEmpty() || province.isEmpty() || district.isEmpty() || detail.isEmpty());
	}

}
