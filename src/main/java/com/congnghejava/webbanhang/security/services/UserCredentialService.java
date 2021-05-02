package com.congnghejava.webbanhang.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.congnghejava.webbanhang.models.UserCredential;
import com.congnghejava.webbanhang.repository.UserCredentialRepository;
import com.congnghejava.webbanhang.security.UserPrincipal;

@Service
public class UserCredentialService {
	@Autowired
	UserCredentialRepository userCredentialRepository;

	public UserCredential getCurrentUserCredential(UserPrincipal userPrincipal) {
		String email = userPrincipal.getName();

		UserCredential userCredential = userCredentialRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

		return userCredential;
	}
}
