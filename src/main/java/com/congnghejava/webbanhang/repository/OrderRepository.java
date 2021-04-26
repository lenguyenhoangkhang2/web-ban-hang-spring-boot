package com.congnghejava.webbanhang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.congnghejava.webbanhang.models.Order;
import com.congnghejava.webbanhang.models.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
	public List<Order> findAllByUser(User user);
}
