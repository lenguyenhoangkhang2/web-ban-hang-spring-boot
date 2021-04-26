package com.congnghejava.webbanhang.payload.response;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.congnghejava.webbanhang.models.EOrderStatus;
import com.congnghejava.webbanhang.models.Order;

public class OrderResponse {
	private String id;
	private Date createdDate;
	private Date updatedDate;
	private EOrderStatus status;

	private List<OrderItemResponse> items;

	public OrderResponse(Order order) {
		this.id = order.getId();
		this.status = order.getStatus();
		this.createdDate = order.getCreatedDate();
		this.updatedDate = order.getUpdatedDate();
		this.items = order.getOrderItems().stream().map(orderItem -> {
			String productName = orderItem.getProductName();
			int price = orderItem.getPrice();
			int quantity = orderItem.getQuantity();
			long total = orderItem.getTotal();

			return new OrderItemResponse(productName, price, quantity, total);
		}).collect(Collectors.toList());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public EOrderStatus getStatus() {
		return status;
	}

	public void setStatus(EOrderStatus status) {
		this.status = status;
	}

	public List<OrderItemResponse> getItems() {
		return items;
	}

	public void setItems(List<OrderItemResponse> items) {
		this.items = items;
	}

}
