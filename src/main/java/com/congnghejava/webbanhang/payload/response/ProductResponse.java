package com.congnghejava.webbanhang.payload.response;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.services.ProductService;

public class ProductResponse {

	@Autowired
	ProductService productService;

	private Long id;
	private String type;
	private String name;
	private String description;
	private String brandName;
	private String categoryName;
	private int price;
	private int discount;
	private int quantity;
	private Long userId;
	private Date createdDate;
	private ProductImageResponse imageUrls;
	private Object details;

	public ProductResponse() {
	}

	public ProductResponse(Product product) {
		this.id = product.getId();
		this.userId = product.getUser().getId();
		this.type = product.getProductType().toString();
		this.name = product.getName();
		this.description = product.getDescription();
		this.brandName = product.getBrand().getName();
		this.categoryName = product.getCategory().getName();
		this.price = product.getPrice();
		this.discount = product.getDiscount();
		this.quantity = product.getQuantity();
		this.imageUrls = new ProductImageResponse(product);
		this.createdDate = product.getCreatedDate();
		this.details = productService.getDetails(product);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public ProductImageResponse getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(ProductImageResponse imageUrls) {
		this.imageUrls = imageUrls;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getDetails() {
		return this.details;
	}

	public void setDetails(Objects details) {
		this.details = details;
	}

}
