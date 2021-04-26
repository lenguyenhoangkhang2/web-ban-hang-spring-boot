package com.congnghejava.webbanhang.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "name")
	private String name;

	@Column(name = "description", columnDefinition = "text")
	private String description;

	@Column(name = "price")
	private int price;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "discount")
	private int discount;

	@OneToOne
	@JoinColumn(name = "file_id")
	private FileDB image;

	@ManyToOne()
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@ManyToOne()
	@JoinColumn(name = "brand_id", nullable = false)
	private Brand brand;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id")
	private Set<Review> reviews = new HashSet<>();

	public Product() {
	}

	public Product(String name, String description, Category category, Brand brand, int price, int quantity,
			int discount) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.brand = brand;
		this.price = price;
		this.quantity = quantity;
		this.discount = discount;
		this.createdDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public FileDB getImage() {
		return image;
	}

	public void setImage(FileDB fileImage) {
		this.image = fileImage;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
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

	public void addReview(Review tempReview) {
		reviews.add(tempReview);
		tempReview.setProduct(this);
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", createdDate=" + createdDate + ", name=" + name + ", price=" + price
				+ ", quantity=" + quantity + ", discount=" + discount + "]";
	}

}