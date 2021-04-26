package com.congnghejava.webbanhang.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
