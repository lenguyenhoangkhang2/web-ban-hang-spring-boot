package com.congnghejava.webbanhang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.congnghejava.webbanhang.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
