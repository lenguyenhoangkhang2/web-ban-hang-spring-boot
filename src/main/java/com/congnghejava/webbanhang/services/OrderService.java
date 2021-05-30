package com.congnghejava.webbanhang.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.congnghejava.webbanhang.exception.BadRequestException;
import com.congnghejava.webbanhang.models.EOrderStatus;
import com.congnghejava.webbanhang.models.EPaymentMethod;
import com.congnghejava.webbanhang.models.Order;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.repository.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;

@Service
public class OrderService {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ProductService productService;

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public Order findById(String id) {
		return orderRepository.findById(id).get();
	}

	public List<Order> findAllByUser(User user) {
		return orderRepository.findAllByUser(user);
	}

	public void updateOrder(Order order) {
		if (EOrderStatus.Open.equals(order.getStatus())) {
			order.setStatus(EOrderStatus.Confirmed);
			order.setUpdatedDate(new Date());
			return;
		}
		if (EOrderStatus.Confirmed.equals(order.getStatus()) || EOrderStatus.Returned.equals(order.getStatus())) {
			order.setStatus(EOrderStatus.Shipping);
			order.setUpdatedDate(new Date());
			return;
		}
		if (EOrderStatus.Shipping.equals(order.getStatus())) {
			order.setStatus(EOrderStatus.Collected);
			order.setUpdatedDate(new Date());
			order.setSettled(true);
			return;
		}
		if (EOrderStatus.Collected.equals(order.getStatus())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Đơn hàng đã được nhận vào ngày " + order.getUpdatedDate());
		}
		if (EOrderStatus.Canceled.equals(order.getStatus())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể cập nhật đơn hàng đã hủy");
		}
	}

	public void returnedOrder(Order order) {
		if (order.getStatus().equals(EOrderStatus.Shipping)) {
			order.setStatus(EOrderStatus.Returned);
			order.setUpdatedDate(new Date());
		} else {
			throw new BadRequestException("Đơn hàng vẫn chưa được gửi. Không thể thực hiện thao tác này!");
		}
	}

	public void canceledOrder(Order order) throws StripeException {
		if (order.getStatus() != EOrderStatus.Canceled && order.getStatus() != EOrderStatus.Collected) {
			order.setStatus(EOrderStatus.Canceled);
			order.setUpdatedDate(new Date());

			order.getOrderItems().stream().forEach(item -> {
				Product product = productService.findById(item.getProductId());
				product.setQuantity(product.getQuantity() + item.getQuantity());
				productService.save(product);
			});

			orderRepository.save(order);
			if (order.getPaymentMethod() == EPaymentMethod.Stripe) {
				Refund.create(RefundCreateParams.builder().setCharge(order.getStripeChagreId()).build());
			}

		} else {
			String message = order.getStatus() == EOrderStatus.Canceled ? "Đơn hàng đã được hủy trước đó"
					: "Không thể hủy đơn hàng đã nhận";
			throw new BadRequestException(message);
		}
	}

	public Order save(Order order) {
		return orderRepository.save(order);
	}

	public void remove(Order order) {
		orderRepository.deleteById(order.getId());
	}
}
