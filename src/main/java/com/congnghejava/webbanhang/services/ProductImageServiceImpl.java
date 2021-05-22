package com.congnghejava.webbanhang.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.congnghejava.webbanhang.models.EProductImageTypeDisplay;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.ProductImage;
import com.congnghejava.webbanhang.repository.ProductImageRepository;

@Service
public class ProductImageServiceImpl implements ProductImageService {

	@Autowired
	ProductImageRepository productImageRepository;

	@Autowired
	FilesStorageService filesSrorageService;

	@Override
	public void deleteByProduct(Product product) {
		productImageRepository.findByProduct(product).stream().forEach(image -> {
			filesSrorageService.delete(image.getName());
		});
		productImageRepository.deleteByProduct(product);
	}

	@Override
	public void save(Product product, String fileName, EProductImageTypeDisplay typeDisplay) {
		productImageRepository.save(new ProductImage(typeDisplay, fileName, product));
	}

	@Override
	public void deleteByProductAndType(Product product, EProductImageTypeDisplay typeImage) {
		productImageRepository.findByProductAndType(product, typeImage).stream().forEach(image -> {
			filesSrorageService.delete(image.getName());
		});
		productImageRepository.deleteByProductAndType(product, typeImage);
	}

	@Override
	public List<ProductImage> findByProductAndType(Product product, EProductImageTypeDisplay typeImage) {
		return productImageRepository.findByProductAndType(product, typeImage);
	}

	@Override
	public Optional<ProductImage> findFirstByProductAndType(Product product, EProductImageTypeDisplay typeImage) {
		return productImageRepository.findFirstByProductAndType(product, typeImage);
	}

}
