package com.congnghejava.webbanhang.repository.criteria;

public class ProductSearchCriteria {
	private String name;
	private String brand;
	private String catorory;
	private Integer minPrice;
	private Integer maxPrice;

	public ProductSearchCriteria() {
		// TODO Auto-generated constructor stub
	}

	public ProductSearchCriteria(String name, String brand, String catorory, int minPrice, int maxPrice) {
		this.name = name;
		this.brand = brand;
		this.catorory = catorory;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}

	public Integer getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Integer minPrice) {
		this.minPrice = minPrice;
	}

	public Integer getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Integer maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCatorory() {
		return catorory;
	}

	public void setCatorory(String catorory) {
		this.catorory = catorory;
	}

}
