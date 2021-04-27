package com.congnghejava.webbanhang.payload.response;

import com.congnghejava.webbanhang.models.Brand;

public class BrandResponse {
	private Long id;
	private String name;

	public BrandResponse(Brand brand) {
		this.id = brand.getId();
		this.name = brand.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	};

}
