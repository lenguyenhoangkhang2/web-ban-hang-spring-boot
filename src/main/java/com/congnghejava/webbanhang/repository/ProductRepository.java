package com.congnghejava.webbanhang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.congnghejava.webbanhang.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	public List<Product> findByNameContaining(String productName);
}
