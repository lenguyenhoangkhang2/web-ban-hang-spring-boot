package com.congnghejava.webbanhang.payload.response;

public class OrderItemResponse {
	private long productId;
	private String productName;
	private int price;
	private int quantity;
	private long total;

	public OrderItemResponse(String productName, int price, int quantity, long total) {
		this.productName = productName;
		this.price = price;
		this.quantity = quantity;
		this.total = total;
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

}
