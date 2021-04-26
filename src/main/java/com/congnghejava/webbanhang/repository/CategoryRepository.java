package com.congnghejava.webbanhang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.congnghejava.webbanhang.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	public boolean existsByNameIgnoreCase(String name);
}
