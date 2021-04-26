package com.congnghejava.webbanhang.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AddProductRequest {
	@NotEmpty(message = "Product name is required")
	private String name;

	@NotEmpty(message = "Description name is required")
	private String description;

	@NotNull(message = "Category name is required")
	private Long categoryId;

	@NotNull(message = "Brand name is required")
	private Long brandId;

	@NotNull(message = "Price is required")
	@Min(value = 0, message = "Price must be greater than 0")
	private int price;

	@Min(value = 0, message = "Quantity must be greater than 0")
	private int quantity = 0;

	@Min(value = 0, message = "Disount must be between 0 and 100")
	@Max(value = 100, message = "Disount must be between 0 and 100")
	private int discount = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
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

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

}
