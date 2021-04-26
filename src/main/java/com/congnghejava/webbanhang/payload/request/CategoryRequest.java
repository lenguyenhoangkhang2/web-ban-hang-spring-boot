package com.congnghejava.webbanhang.payload.request;

import javax.validation.constraints.NotEmpty;

public class CategoryRequest {
	@NotEmpty(message = "Category name may not empty!")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
