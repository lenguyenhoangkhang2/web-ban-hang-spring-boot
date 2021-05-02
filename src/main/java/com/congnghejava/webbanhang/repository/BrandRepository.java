package com.congnghejava.webbanhang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.congnghejava.webbanhang.models.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
	public boolean existsByNameIgnoreCase(String name);

	public List<Brand> findByNameContainingIgnoreCase(String name);
}
