package com.congnghejava.webbanhang.payload.response;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.congnghejava.webbanhang.models.Cart;
import com.congnghejava.webbanhang.models.EProductImageTypeDisplay;
import com.congnghejava.webbanhang.models.ProductImage;
import com.congnghejava.webbanhang.services.ProductImageServiceImpl;

public class CartResponse {
	@Autowired
	ProductImageServiceImpl productImageService;

	private Long cartId;

	private String productImageUrl;

	private String productName;

	private int quantity;

	private long total;

	public CartResponse(Cart cart) {
		Optional<ProductImage> image = productImageService.findFirstByProductAndType(cart.getProduct(),
				EProductImageTypeDisplay.Official);
		if (image.isPresent()) {
			this.productImageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
					.path(image.get().getName()).toUriString();
		}

		this.cartId = cart.getId();
		this.productName = cart.getProduct().getName();
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
