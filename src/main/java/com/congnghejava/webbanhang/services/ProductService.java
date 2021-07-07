package com.congnghejava.webbanhang.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.ProductPage;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.payload.response.ProductResponse;
import com.congnghejava.webbanhang.repository.ProductCriteriaRepository;
import com.congnghejava.webbanhang.repository.ProductRepository;
import com.congnghejava.webbanhang.repository.criteria.ProductSearchCriteria;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductCriteriaRepository productCriteriaRepository;

	@Autowired
	FileStorageServiceImpl fileStorageServiceImpl;

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public Page<ProductResponse> findAllWithFilter(ProductPage productPage,
			ProductSearchCriteria productSearchCriteria) {
		return productCriteriaRepository.findAllWithFilter(productPage, productSearchCriteria);
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
