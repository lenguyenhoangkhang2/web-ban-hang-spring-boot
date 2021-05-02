package com.congnghejava.webbanhang.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.repository.UserRepository;
import com.congnghejava.webbanhang.security.UserPrincipal;
import com.congnghejava.webbanhang.security.services.UserCredentialService;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserCredentialService userCredentialService;

	public User getCurrentUser(UserPrincipal userPrincipal) {
		return userCredentialService.getCurrentUserCredential(userPrincipal).getUser();
	}
}
