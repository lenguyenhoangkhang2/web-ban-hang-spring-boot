package com.congnghejava.webbanhang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.congnghejava.webbanhang.models.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
