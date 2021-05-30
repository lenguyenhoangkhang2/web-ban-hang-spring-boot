package com.congnghejava.webbanhang.payload.response;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import com.congnghejava.webbanhang.models.Order;

public class OrderResponse {
	private String id;
	private String createdDate;
	private String updatedDate;
	private String status;
	private long total;

	private List<OrderItemResponse> items;

	public OrderResponse(Order order) {
		this.id = order.getId();
		this.status = order.getStatus().toString();
		this.createdDate = new SimpleDateFormat("dd/MM/yyyy").format(order.getCreatedDate());
		this.updatedDate = new SimpleDateFormat("dd/MM/yyyy").format(order.getUpdatedDate());
		this.items = order.getOrderItems().stream().map(orderItem -> {

			String productName = orderItem.getProductName();
			int price = orderItem.getPrice();
			int quantity = orderItem.getQuantity();
			long total = orderItem.getTotal();

			return new OrderItemResponse(productName, price, quantity, total);
		}).collect(Collectors.toList());
		this.total = order.getTotal();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<OrderItemResponse> getItems() {
		return items;
	}

	public void setItems(List<OrderItemResponse> items) {
		this.items = items;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}
