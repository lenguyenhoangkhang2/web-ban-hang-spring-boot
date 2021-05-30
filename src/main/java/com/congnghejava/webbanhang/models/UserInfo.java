package com.congnghejava.webbanhang.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "country")
	private String country;

	@Column(name = "provine")
	private String provine;

	@Column(name = "district")
	private String district;

	@Column(name = "detail")
	private String detail;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Address(String country, String provine, String district, String detail, User user) {
		this.country = country;
		this.provine = provine;
		this.district = district;
		this.detail = detail;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvine() {
		return provine;
	}

	public void setProvine(String provine) {
		this.provine = provine;
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

}
