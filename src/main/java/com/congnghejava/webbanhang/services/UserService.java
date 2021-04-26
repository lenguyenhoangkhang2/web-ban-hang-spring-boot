package com.congnghejava.webbanhang.services;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserCredentialService userCredentialService;

	public User getCurrentUser(Principal principal) {
		return userCredentialService.getCurrentUserCredential(principal).getUser();
	}
}
