package com.congnghejava.webbanhang.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.congnghejava.webbanhang.models.EOrderStatus;
import com.congnghejava.webbanhang.models.Order;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.repository.OrderRepository;

@Service
public class OrderService {
	@Autowired
	OrderRepository orderRepository;

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
			return;
		}
		if (EOrderStatus.Collected.equals(order.getStatus())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Order is collected at " + order.getUpdatedDate());
		}
	}

	public void returnedOrder(Order order) {
		if (order.getStatus().equals(EOrderStatus.Shipping) && !order.getStatus().equals(EOrderStatus.Collected)) {
			order.setStatus(EOrderStatus.Returned);
			order.setUpdatedDate(new Date());
		}
	}

	public void canceledOrder(Order order) {
		if (!order.getStatus().equals(EOrderStatus.Collected)) {
			order.setStatus(EOrderStatus.Canceled);
			order.setUpdatedDate(new Date());
		}
	}

	public Order save(Order order) {
		return orderRepository.save(order);
	}

	public void remove(Order order) {
		orderRepository.deleteById(order.getId());
	}
}
