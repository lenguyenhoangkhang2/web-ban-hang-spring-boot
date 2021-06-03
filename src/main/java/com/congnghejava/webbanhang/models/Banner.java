package com.congnghejava.webbanhang.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "banner")
public class Banner {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "product_id")
	private ProductImage banner;

	public Banner() {

	}

	public Banner(ProductImage productImage) {
		this.banner = productImage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProductImage getBanner() {
		return banner;
	}

	public void setBanner(ProductImage banner) {
		this.banner = banner;
	}

}
