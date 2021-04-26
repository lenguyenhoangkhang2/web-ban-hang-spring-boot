package com.congnghejava.webbanhang.payload.response;

import java.util.Date;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.congnghejava.webbanhang.models.FileDB;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.services.BrandServiceImpl;
import com.congnghejava.webbanhang.services.CategoryServiceImpl;
import com.congnghejava.webbanhang.services.FileStorageService;

public class ProductResponse {

	private Long id;
	private String name;
	private String description;
	private String brandName;
	private String categoryName;
	private int price;
	private int discount;
	private int quantity;
	private Long userId;
	private Date createdDate;
	private String imageUrl;

	public ProductResponse() {

	}

	public ProductResponse(Long id, String name, String description, String brandName, String categoryName, int price,
			int discount, int quantity, Long userId, Date createdDate, String imageUrl) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.brandName = brandName;
		this.categoryName = categoryName;
		this.price = price;
		this.discount = discount;
		this.quantity = quantity;
		this.userId = userId;
		this.createdDate = createdDate;
		this.imageUrl = imageUrl;
	}

	public ProductResponse(Product product, FileStorageService fileStorageService, BrandServiceImpl brandService,
			CategoryServiceImpl categoryService) {
		String imageUrl = product.getImage().getId();
		FileDB fileImage = fileStorageService.findById(imageUrl);

		this.id = product.getId();
		this.userId = product.getUser().getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.brandName = brandService.findById(product.getBrand().getId()).get().getName();
		this.categoryName = categoryService.findById(product.getCategory().getId()).get().getName();
		this.price = product.getPrice();
		this.discount = product.getDiscount();
		this.quantity = product.getQuantity();
		this.imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/").path(fileImage.getId())
				.toUriString();
		this.createdDate = product.getCreatedDate();
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

	public String getImageUri() {
		return imageUrl;
	}

	public void setImageUri(String imageUri) {
		this.imageUrl = imageUri;
	}

}
