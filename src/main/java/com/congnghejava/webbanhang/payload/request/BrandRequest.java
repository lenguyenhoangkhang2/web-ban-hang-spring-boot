package com.congnghejava.webbanhang.payload.request;

import javax.validation.constraints.NotEmpty;

public class BrandRequest {
	@NotEmpty(message = "Brand name may not empty!")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
