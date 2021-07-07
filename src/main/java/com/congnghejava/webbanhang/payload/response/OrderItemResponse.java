package com.congnghejava.webbanhang.payload.response;

import com.congnghejava.webbanhang.models.OrderItem;

public class OrderItemResponse {
	private long productId;
	private String productName;
	private int price;
	private int discount;
	private int quantity;
	private long total;

	public OrderItemResponse(OrderItem orderItem) {
		this.productName = orderItem.getProductName();
		this.price = orderItem.getPrice();
		this.quantity = orderItem.getQuantity();
		this.discount = orderItem.getDiscount();
		this.total = orderItem.getTotal();
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

}
