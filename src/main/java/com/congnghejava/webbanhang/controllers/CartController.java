package com.congnghejava.webbanhang.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.congnghejava.webbanhang.models.Cart;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.payload.request.CartRequest;
import com.congnghejava.webbanhang.payload.response.CartResponse;
import com.congnghejava.webbanhang.repository.CartService;
import com.congnghejava.webbanhang.security.UserPrincipal;
import com.congnghejava.webbanhang.services.ProductService;
import com.congnghejava.webbanhang.services.UserService;

@RestController
@RequestMapping("/cart")
public class CartController {
	@Autowired
	UserService userService;

	@Autowired
	CartService cartService;

	@Autowired
	ProductService productService;

	@GetMapping
	public ResponseEntity<?> getAllCart(UserPrincipal userPrincipal) {
		User user = userService.getCurrentUser(userPrincipal);
		List<CartResponse> cartResponse = cartService.findAllByUser(user).stream().map(cart -> new CartResponse(cart))
				.collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
	}

	@PostMapping
	public ResponseEntity<?> addCart(@RequestBody CartRequest cartRequest, UserPrincipal userPrincipal) {
		Product product = productService.findById(cartRequest.getProductId());

		User user = userService.getCurrentUser(userPrincipal);

		if (product.getQuantity() < cartRequest.getQuantity()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There are not enough product in stock!");
		}

		if (cartService.existsByProductAndUser(product, user)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product is in your cart!");
		}

		product.setQuantity(product.getQuantity() - cartRequest.getQuantity());

		Cart cart = new Cart(user, product, cartRequest.getQuantity());
		cartService.save(cart);

		return ResponseEntity.status(HttpStatus.OK).body("Product has been added to your cart!");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCart(@PathVariable Long id) {
		Cart cart = cartService.findById(id);
		Product product = productService.findById(cart.getProduct().getId());

		product.setQuantity(product.getQuantity() + cart.getQuantity());
		cartService.remove(id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(cart.getProduct().getName() + " has been removed from your cart!");
	}
}
