package com.congnghejava.webbanhang.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.congnghejava.webbanhang.config.AppProperties;
import com.congnghejava.webbanhang.exception.BadRequestException;
import com.congnghejava.webbanhang.models.Cart;
import com.congnghejava.webbanhang.models.ERole;
import com.congnghejava.webbanhang.models.Order;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.payload.response.MessageResponse;
import com.congnghejava.webbanhang.payload.response.OrderItemResponse;
import com.congnghejava.webbanhang.payload.response.OrderResponse;
import com.congnghejava.webbanhang.payload.response.UpdateOrderResponse;
import com.congnghejava.webbanhang.repository.criteria.OrderSearchCriteria;
import com.congnghejava.webbanhang.security.CurrentUser;
import com.congnghejava.webbanhang.security.UserPrincipal;
import com.congnghejava.webbanhang.services.CartService;
import com.congnghejava.webbanhang.services.OrderService;
import com.congnghejava.webbanhang.services.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	UserService userService;

	@Autowired
	OrderService orderService;

	@Autowired
	CartService cartService;

	@Autowired
	AppProperties appProperties;

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Page<OrderResponse>> getAllOrder(
			@RequestParam(name = "username", required = false, defaultValue = "") String username,
			@RequestParam(name = "status", required = false, defaultValue = "") String status,
			@RequestParam(name = "id", required = false, defaultValue = "") String id,
			@PageableDefault(page = 0, size = 1, sort = { "createdDate" }, direction = Direction.DESC) Pageable page) {

		OrderSearchCriteria searchCriteria = new OrderSearchCriteria(id, username, status);

		Page<Order> pageOrder = orderService.findWithFilter(searchCriteria, page);
		List<OrderResponse> listOrderResponse = pageOrder.stream().map(order -> new OrderResponse(order))
				.collect(Collectors.toList());

		Page<OrderResponse> response = new PageImpl<>(listOrderResponse, page, pageOrder.getTotalElements());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/user")
	public ResponseEntity<?> getAllByUser(@CurrentUser UserPrincipal userPrincipal,
			@PageableDefault(page = 0, size = 1, sort = { "createdDate" }, direction = Direction.DESC) Pageable page) {
		User user = userService.getCurrentUser(userPrincipal);

		Page<Order> pageOrder = orderService.findAllByUser(user, page);
		List<OrderResponse> listOrderResponse = pageOrder.stream().map(order -> new OrderResponse(order))
				.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new PageImpl<>(listOrderResponse, page, pageOrder.getTotalElements()));
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getOrder(@PathVariable String id) {
		Order order = orderService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(new OrderResponse(order));
	}

	@GetMapping("/{id}/items")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getOrderItems(@PathVariable String id) {
		Order order = orderService.findById(id);
		List<OrderItemResponse> listOrderItems = order.getOrderItems().stream().map(item -> new OrderItemResponse(item))
				.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(listOrderItems);
	}

	@PostMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> addOrder(@CurrentUser UserPrincipal userPrincipal) {
		User user = userService.getCurrentUser(userPrincipal);
		List<Cart> carts = cartService.findByUserAndEnable(user, true);

		if (carts.size() == 0) {
			throw new BadRequestException("Hiện tại không có sản phẩm trong giỏ hàng");
		}

		if (!user.getUserInfo().isFullfill()) {
			throw new BadRequestException("Thông tin giao hàng còn thiếu!");
		}

		Order order = new Order(user, carts);
		orderService.save(order);

		carts.forEach(cart -> cartService.remove(cart.getId()));

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Bạn đã đặt hàng thành công"));
	}

	@PutMapping(value = "/{id}/update")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateStatusOrder(@PathVariable String id) throws MailException, MessagingException {
		Order order = orderService.findById(id);
		orderService.updateOrder(order);

		orderService.save(order);

		orderService.sendOrderStatusMail(order);

		return ResponseEntity.status(HttpStatus.OK).body(new UpdateOrderResponse(order.getStatus()));
	}

	@PutMapping(path = "/{id}/return")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> returnedOrder(@PathVariable String id) throws MailException, MessagingException {
		Order order = orderService.findById(id);
		orderService.returnedOrder(order);

		orderService.save(order);

		orderService.sendOrderStatusMail(order);

		return ResponseEntity.status(HttpStatus.OK).body("Order " + order.getId() + " updated to status: Returned");
	}

	@PostConstruct
	public void init() {
		Stripe.apiKey = appProperties.getStripe().getTokenSecret();
	}

	@PutMapping(path = "/{id}", params = "cancel")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> canceledOrder(@CurrentUser UserPrincipal userPrincipal, @PathVariable String id)
			throws StripeException, MailException, MessagingException {
		User user = userService.getCurrentUser(userPrincipal);
		Order order = orderService.findById(id);

		boolean hasRoleAdmin = user.getUserCredential().getRoles().stream()
				.anyMatch(role -> role.getName() == ERole.ROLE_ADMIN);

		if (user == order.getUser() || hasRoleAdmin) {
			orderService.canceledOrder(order);
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Đã hủy đơn hàng!"));
		}

		orderService.sendOrderStatusMail(order);

		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new MessageResponse("Không có quyền truy cập thông tin này"));
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<?> deleteOrder(@PathVariable String id) {
		Order order = orderService.findById(id);
		orderService.remove(order);
		return ResponseEntity.status(HttpStatus.OK).body("Order " + order.getId() + " has already been deleted");
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(ex.getLocalizedMessage()));
	}
}
