package com.congnghejava.webbanhang.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.congnghejava.webbanhang.models.Review;
import com.congnghejava.webbanhang.repository.ReviewRepository;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	ReviewRepository reviewRepository;

	@Override
	public List<Review> findAll() {
		return reviewRepository.findAll();
	}

	@Override
	public Optional<Review> findById(Long theId) {
		return reviewRepository.findById(theId);
	}

	@Override
	public Review save(Review theReview) {
		return reviewRepository.save(theReview);
	}

	@Override
	public void remove(Long theId) {
		reviewRepository.deleteById(theId);
	}

}
