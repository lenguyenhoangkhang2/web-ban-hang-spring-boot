package com.congnghejava.webbanhang.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.congnghejava.webbanhang.exception.ResourceNotFoundException;
import com.congnghejava.webbanhang.models.UserCredential;
import com.congnghejava.webbanhang.payload.response.UserInfoResponse;
import com.congnghejava.webbanhang.repository.UserCredentialRepository;
import com.congnghejava.webbanhang.security.CurrentUser;
import com.congnghejava.webbanhang.security.UserPrincipal;

@RestController
public class UserController {

	@Autowired
	private UserCredentialRepository userCredentialRepository;

	@GetMapping("/user/me")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
		UserCredential userCredential = userCredentialRepository.findById(userPrincipal.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

		System.out.println(new UserInfoResponse(userCredential).getAuthorities());

		return ResponseEntity.status(HttpStatus.OK).body(new UserInfoResponse(userCredential));
	}

}
