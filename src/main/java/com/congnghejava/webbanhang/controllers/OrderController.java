package com.congnghejava.webbanhang.controllers;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.congnghejava.webbanhang.models.Cart;
import com.congnghejava.webbanhang.models.Order;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.payload.response.OrderResponse;
import com.congnghejava.webbanhang.repository.CartService;
import com.congnghejava.webbanhang.services.OrderService;
import com.congnghejava.webbanhang.services.UserService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	@Autowired
	UserService userService;

	@Autowired
	OrderService orderService;

	@Autowired
	CartService cartService;

	@GetMapping
	public ResponseEntity<?> getAllOrder() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(orderService.findAll().stream().map(order -> new OrderResponse(order)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrder(@PathVariable String id) {
		Order order = orderService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(new OrderResponse(order));
	}

	@GetMapping(params = "userId")
	public ResponseEntity<?> getAllByUser(@PathParam(value = "userId") Long userId, Principal principal) {
		User user = userService.getCurrentUser(principal);

		List<OrderResponse> orderResponses = orderService.findAllByUser(user).stream()
				.map(order -> new OrderResponse(order)).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(orderResponses);
	}

	@PostMapping
	public ResponseEntity<?> addOrder(Principal principal) {
		User user = userService.getCurrentUser(principal);
		List<Cart> carts = cartService.findAllByUser(user);

		Order order = new Order(user, carts);
		orderService.save(order);

		carts.forEach(cart -> cartService.remove(cart.getId()));

		return ResponseEntity.status(HttpStatus.OK).body("Your order is ready!");
	}

	@PutMapping(value = "/{id}", params = "update")
	public ResponseEntity<?> updateStatusOrder(@PathVariable String id) {
		Order order = orderService.findById(id);
		orderService.updateOrder(order);

		orderService.save(order);
		return ResponseEntity.status(HttpStatus.OK)
				.body("Order " + order.getId() + " updated to status: " + order.getStatus());
	}

	@PutMapping(path = "/{id}", params = "return")
	public ResponseEntity<?> returnedOrder(@PathVariable String id) {
		Order order = orderService.findById(id);
		orderService.returnedOrder(order);

		orderService.save(order);
		return ResponseEntity.status(HttpStatus.OK).body("Order " + order.getId() + " updated to status: Returned");
	}

	@PutMapping(path = "/{id}", params = "cancel")
	public ResponseEntity<?> canceledOrder(@PathVariable String id) {
		Order order = orderService.findById(id);
		orderService.canceledOrder(order);

		orderService.save(order);
		return ResponseEntity.status(HttpStatus.OK).body("Order " + order.getId() + " updated to status: Canceled");
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<?> deleteOrder(@PathVariable String id) {
		Order order = orderService.findById(id);
		orderService.remove(order);
		return ResponseEntity.status(HttpStatus.OK).body("Order " + order.getId() + " has already been deleted");
	}
}
