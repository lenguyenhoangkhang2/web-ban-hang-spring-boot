package com.congnghejava.webbanhang.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_images")
public class ProductImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private EProductImageTypeDisplay type;

	@Column(name = "name")
	private String name;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	public ProductImage() {

	}

	public ProductImage(EProductImageTypeDisplay type, String name, Product product) {
		this.type = type;
		this.name = name;
		this.product = product;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EProductImageTypeDisplay getType() {
		return type;
	}

	public void setType(EProductImageTypeDisplay type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
