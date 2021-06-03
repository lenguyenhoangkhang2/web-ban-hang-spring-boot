package com.congnghejava.webbanhang.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.congnghejava.webbanhang.models.ProductImage;

@RestController
@RequestMapping("/header")
public class HeaderDisplayController {
	@GetMapping("/banner")
	public ResponseEntity<ProductImage> getBannerImage() {
		return null;
	}
}
