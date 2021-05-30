package com.congnghejava.webbanhang.payload.response;

import com.congnghejava.webbanhang.models.EOrderStatus;

public class UpdateOrderResponse {
	private String orderStatus;

	public UpdateOrderResponse(EOrderStatus status) {
		orderStatus = status.toString();
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

}
