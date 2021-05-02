package com.congnghejava.webbanhang.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.congnghejava.webbanhang.exception.ResourceNotFoundException;
import com.congnghejava.webbanhang.models.UserCredential;
import com.congnghejava.webbanhang.repository.UserCredentialRepository;
import com.congnghejava.webbanhang.security.CurrentUser;
import com.congnghejava.webbanhang.security.UserPrincipal;

@RestController
public class UserController {

	@Autowired
	private UserCredentialRepository userCredentialRepository;

	@GetMapping("/user/me")
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserCredential getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
		return userCredentialRepository.findById(userPrincipal.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
	}

}
