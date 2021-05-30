package com.congnghejava.webbanhang.payload.response;

import java.util.Optional;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.congnghejava.webbanhang.models.Cart;
import com.congnghejava.webbanhang.models.EProductImageTypeDisplay;
import com.congnghejava.webbanhang.models.ProductImage;

public class CartResponse {

	private Long cartId;

	private Boolean enable;

	private String productImageUrl;

	private String productName;

	private int quantity;

	private int productPrice;

	private int productDiscount;

	public CartResponse(Cart cart) {
		Optional<ProductImage> productImageOfficial = cart.getProduct().getImages().stream()
				.filter(image -> image.getType() == EProductImageTypeDisplay.Official).findAny();

		if (productImageOfficial.isPresent()) {
			this.productImageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
					.path(productImageOfficial.get().getName()).toUriString();
		}

		this.cartId = cart.getId();
		this.productName = cart.getProduct().getName();
		this.quantity = cart.getQuantity();
		this.productPrice = cart.getProduct().getPrice();
		this.productDiscount = cart.getProduct().getDiscount();
		this.enable = cart.getEnable();
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

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public int getProductDiscount() {
		return productDiscount;
	}

	public void setProductDiscount(int productDiscount) {
		this.productDiscount = productDiscount;
	}

	public Boolean isEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

}
