package com.congnghejava.webbanhang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.congnghejava.webbanhang.models.UserCredential;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

	Optional<UserCredential> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

}
