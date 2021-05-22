package com.congnghejava.webbanhang.services;

import java.util.List;
import java.util.Optional;

import com.congnghejava.webbanhang.models.EProductImageTypeDisplay;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.ProductImage;

public interface ProductImageService {

	public List<ProductImage> findByProductAndType(Product product, EProductImageTypeDisplay typeImage);

	public Optional<ProductImage> findFirstByProductAndType(Product product, EProductImageTypeDisplay typeImage);

	public void deleteByProduct(Product product);

	public void deleteByProductAndType(Product product, EProductImageTypeDisplay typeImage);

	public void save(Product product, String fileName, EProductImageTypeDisplay type);
}
