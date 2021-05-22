package com.congnghejava.webbanhang.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.congnghejava.webbanhang.models.LaptopDetails;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.repository.LaptopDetailsRepository;

@Service
public class LaptopDetailsServiceImpl implements LaptopDetailsService {
	@Autowired
	LaptopDetailsRepository laptopDetailsRepository;

	@Override
	public void deleteByProduct(Product product) {
		laptopDetailsRepository.deleteByProduct(product);
	}

	@Override
	public LaptopDetails save(LaptopDetails laptopDetails) {
		return laptopDetailsRepository.save(laptopDetails);
	}

	@Override
	public LaptopDetails findByProduct(Product product) {
		return laptopDetailsRepository.findByProduct(product);
	}

}
