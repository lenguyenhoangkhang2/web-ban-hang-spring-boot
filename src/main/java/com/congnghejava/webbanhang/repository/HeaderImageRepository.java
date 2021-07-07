package com.congnghejava.webbanhang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.congnghejava.webbanhang.models.EHeaderImageType;
import com.congnghejava.webbanhang.models.HeaderImage;

@Repository
public interface HeaderImageRepository extends JpaRepository<HeaderImage, Integer> {
	public List<HeaderImage> findByTypeAndEnable(EHeaderImageType type, Boolean enable);

	public List<HeaderImage> findByType(EHeaderImageType type);
}
