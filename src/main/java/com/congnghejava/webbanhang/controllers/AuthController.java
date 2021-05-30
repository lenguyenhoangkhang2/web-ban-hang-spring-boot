package com.congnghejava.webbanhang.controllers;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.congnghejava.webbanhang.exception.BadRequestException;
import com.congnghejava.webbanhang.models.AuthProvider;
import com.congnghejava.webbanhang.models.ERole;
import com.congnghejava.webbanhang.models.Role;
import com.congnghejava.webbanhang.models.UserCredential;
import com.congnghejava.webbanhang.payload.request.LoginRequest;
import com.congnghejava.webbanhang.payload.request.SignupRequest;
import com.congnghejava.webbanhang.payload.response.ApiResponse;
import com.congnghejava.webbanhang.payload.response.AuthResponse;
import com.congnghejava.webbanhang.repository.RoleRepository;
import com.congnghejava.webbanhang.repository.UserCredentialRepository;
import com.congnghejava.webbanhang.security.jwtToken.TokenProvider;
import com.congnghejava.webbanhang.utils.UrlImageUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {
	UrlImageUtils urlImageUtils = new UrlImageUtils();

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserCredentialRepository userCredentialRepository;

	@Autowired
	RoleRepository RoleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	TokenProvider tokenProvider;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = tokenProvider.createToken(authentication);

		return ResponseEntity.ok(new AuthResponse(token));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

		if (userCredentialRepository.existsByEmail(signupRequest.getEmail())) {
			throw new BadRequestException("Email address already in use.");
		}

		String name = signupRequest.getName();
		UserCredential user = new UserCredential(signupRequest.getEmail(),
				passwordEncoder.encode(signupRequest.getPassword()), AuthProvider.local);

		Role roleUser = RoleRepository.findByName(ERole.ROLE_USER).get();
		user.setRoles(new HashSet<>(Arrays.asList(roleUser)));
		user.getUser().setName(name);
		user.getUser().setAvatarUrl(urlImageUtils.buildPathWithName("default-avatar.png"));

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("user/me").buildAndExpand(user.getId())
				.toUri();

		userCredentialRepository.save(user);

		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
	}
}
