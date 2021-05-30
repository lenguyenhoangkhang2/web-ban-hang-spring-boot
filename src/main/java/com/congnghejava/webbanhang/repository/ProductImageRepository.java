package com.congnghejava.webbanhang.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.congnghejava.webbanhang.models.EProductImageTypeDisplay;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
	public List<ProductImage> findByProduct(Product product);

	public List<ProductImage> findByProductAndType(Product product, EProductImageTypeDisplay type);

	public Optional<ProductImage> findFirstByProductAndType(Product product, EProductImageTypeDisplay type);

}
