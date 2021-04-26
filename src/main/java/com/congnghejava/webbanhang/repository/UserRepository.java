package com.congnghejava.webbanhang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.congnghejava.webbanhang.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
