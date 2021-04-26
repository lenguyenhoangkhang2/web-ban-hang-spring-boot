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
@Table(name = "orders_items")
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "price")
	private int price;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "total")
	private long total;

	public OrderItem() {
	}

	public OrderItem(Order order, Cart cart) {
		this.order = order;
		this.productName = cart.getProduct().getName();
		this.price = cart.getProduct().getPrice();
		this.quantity = cart.getQuantity();
		this.total = cart.getTotal();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
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

}
