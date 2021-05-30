package com.congnghejava.webbanhang.payload.request;

import javax.validation.constraints.NotNull;

public class CartRequest {
	@NotNull
	private Boolean enable;
	@NotNull
	private int quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

}
