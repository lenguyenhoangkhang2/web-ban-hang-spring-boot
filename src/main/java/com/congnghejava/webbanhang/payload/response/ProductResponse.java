package com.congnghejava.webbanhang.payload.response;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.congnghejava.webbanhang.models.EProductCategory;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.Review;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductResponse {

	private Long id;
	private String name;
	private String description;
	private String categoryName;
	private String brandName;
	private int price;
	private int discount;
	private int quantity;
	private Double rating;
	private int reviewCount;
	private Long userId;
	private Date createdDate;
	private List<ProductImageResponse> images;
	private Object details;

	public ProductResponse() {
	}

	public ProductResponse(Product product) {
		System.out.println(product.getReviews());
		this.id = product.getId();
		this.userId = product.getUser().getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.brandName = product.getBrand().getName().toString();
		this.categoryName = product.getCategory().getName().toString();
		this.price = product.getPrice();
		this.discount = product.getDiscount();
		this.quantity = product.getQuantity();
		this.createdDate = product.getCreatedDate();
		this.reviewCount = product.getReviews().size();
		if (reviewCount > 0) {
			this.rating = Math
					.ceil(product.getReviews().stream().mapToDouble(Review::getRating).average().getAsDouble() * 2) / 2;
		}

		EProductCategory category = EProductCategory.valueOf(product.getCategory().getName());
		switch (category) {
		case Laptop:
			this.details = new LaptopDetailsResponse(product.getDetails());
			break;
		case SmartPhone:
			this.details = new SmartPhoneDetailsResponse(product.getDetails());
		default:
			break;
		}

		this.images = product.getImages().stream().map(productImage -> new ProductImageResponse(productImage))
				.collect(Collectors.toList());
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

	public List<ProductImageResponse> getImages() {
		return images;
	}

	public void setImages(List<ProductImageResponse> images) {
		this.images = images;
	}

	public void setDetails(Object details) {
		this.details = details;
	}

	public Object getDetails() {
		return this.details;
	}

	public void setDetails(Objects details) {
		this.details = details;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

}
