package com.congnghejava.webbanhang.services;

import java.util.List;

import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.Review;
import com.congnghejava.webbanhang.models.User;

public interface ReviewService extends IGeneralService<Review> {

	public List<Review> findByProduct(Product product);

	public boolean existsByUserAndProduct(User user, Product product);

	public Review findByUser(User user);
}
