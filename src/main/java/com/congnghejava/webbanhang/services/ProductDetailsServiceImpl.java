package com.congnghejava.webbanhang.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.congnghejava.webbanhang.models.ProductDetails;
import com.congnghejava.webbanhang.repository.ProductDetailsRepository;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {
	@Autowired
	ProductDetailsRepository productDetailsRepository;

	@Override
	public ProductDetails save(ProductDetails productDetails) {
		return productDetailsRepository.save(productDetails);
	}

}
