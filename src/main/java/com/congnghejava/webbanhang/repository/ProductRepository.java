package com.congnghejava.webbanhang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.congnghejava.webbanhang.models.Brand;
import com.congnghejava.webbanhang.models.Category;
import com.congnghejava.webbanhang.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	public List<Product> findByNameContaining(String productName);

	public List<Product> findByNameContainingIgnoreCaseAndPriceBetween(String productName, int priceStart,
			int priceEnd);

	public List<Product> findByBrandAndPriceBetween(Brand brand, int priceStart, int priceEnd);

	public List<Product> findByCategoryAndPriceBetween(Category brand, int priceStart, int priceEnd);

	public List<Product> findByBrandAndCategoryAndPriceBetween(Brand brand, Category category, int priceStart,
			int priceEnd);
}
