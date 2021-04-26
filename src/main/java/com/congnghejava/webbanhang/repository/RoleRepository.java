package com.congnghejava.webbanhang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.congnghejava.webbanhang.models.ERole;
import com.congnghejava.webbanhang.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByName(ERole Name);
}
