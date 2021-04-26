package com.congnghejava.webbanhang.payload.response;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.congnghejava.webbanhang.models.Cart;

public class CartResponse {
	private Long cartId;

	private String productImageUrl;

	private String productName;

	private int quantity;

	private long total;

	public CartResponse(Cart cart) {
		this.cartId = cart.getId();
		this.productName = cart.getProduct().getName();
		this.productImageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
				.path(cart.getProduct().getImage().getId()).toUriString();
		this.quantity = cart.getQuantity();
		this.total = cart.getTotal();
	}

	public String getProductImage() {
		return productImageUrl;
	}

	public void setProductImage(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public String getProductImageUrl() {
		return productImageUrl;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}
