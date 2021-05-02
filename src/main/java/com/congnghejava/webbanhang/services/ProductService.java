package com.congnghejava.webbanhang.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.congnghejava.webbanhang.models.Brand;
import com.congnghejava.webbanhang.models.Category;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	FileStorageService fileStorageService;

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public List<Product> findByBrandAndCategoryAndPriceBetween(Brand brand, Category category, int priceStart,
			int priceEnd) {
		return productRepository.findByBrandAndCategoryAndPriceBetween(brand, category, priceStart, priceEnd);
	}

	public Product findById(Long theId) {
		return productRepository.findById(theId).get();
	}

	public Product save(Product theProduct) {
		return productRepository.save(theProduct);
	}

	public Product save(Product theProduct, User user) {
		theProduct.setUser(user);
		return productRepository.save(theProduct);
	}

	public void remove(Long theId) {
		productRepository.deleteById(theId);
	}

	public List<Product> findByFilter(String name, Long brandId, Long categoryId, int priceStart, int priceEnd) {
		List<Product> products = productRepository.findAll();

		if (!name.isEmpty()) {
			products = products.stream().filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
					.collect(Collectors.toList());
		}
		if (brandId != 0) {
			products = products.stream().filter(product -> product.getBrand().getId() == brandId)
					.collect(Collectors.toList());
		}

		if (categoryId != 0) {
			products = products.stream().filter(product -> product.getCategory().getId() == categoryId)
					.collect(Collectors.toList());
		}

		products = products.stream()
				.filter(product -> product.getPrice() >= priceStart && product.getPrice() <= priceEnd)
				.collect(Collectors.toList());

		return products;
	}
}
