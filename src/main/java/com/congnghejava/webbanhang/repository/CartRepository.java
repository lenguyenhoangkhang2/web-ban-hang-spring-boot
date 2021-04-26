package com.congnghejava.webbanhang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.congnghejava.webbanhang.models.Cart;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	public List<Cart> findAllByUser(User user);

	public boolean existsByProductAndUser(Product product, User user);
}
