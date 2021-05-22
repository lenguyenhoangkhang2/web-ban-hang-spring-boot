package com.congnghejava.webbanhang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.congnghejava.webbanhang.models.Product;

@NoRepositoryBean
public interface ProductDetailsGeneralRepository<T> extends JpaRepository<T, Long> {
	public void deleteByProduct(Product product);

	public T findByProduct(Product product);
}
