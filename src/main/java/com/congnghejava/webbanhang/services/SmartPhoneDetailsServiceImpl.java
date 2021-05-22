package com.congnghejava.webbanhang.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.SmartPhoneDetails;
import com.congnghejava.webbanhang.repository.SmartPhoneDetailsRepository;

@Service
public class SmartPhoneDetailsServiceImpl implements SmartPhoneDetailsService {

	@Autowired
	SmartPhoneDetailsRepository smartPhoneDetailsRepository;

	@Override
	public void deleteByProduct(Product product) {
		smartPhoneDetailsRepository.deleteByProduct(product);
	}

	@Override
	public SmartPhoneDetails save(SmartPhoneDetails smartPhoneDetails) {
		return smartPhoneDetailsRepository.save(smartPhoneDetails);
	}

	@Override
	public SmartPhoneDetails findByProduct(Product product) {
		return smartPhoneDetailsRepository.findByProduct(product);
	}

}
