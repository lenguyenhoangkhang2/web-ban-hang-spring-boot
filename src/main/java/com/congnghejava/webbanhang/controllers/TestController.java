package com.congnghejava.webbanhang.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/test")
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CLERK') or hasRole('ROLE_ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/clerk")
	@PreAuthorize("hasRole('ROLE_CLERK') or hasRole('ROLE_ADMIN')")
	public String clerkAccess() {
		return "Clerk Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}