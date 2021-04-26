package com.congnghejava.webbanhang.services;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.congnghejava.webbanhang.models.UserCredential;
import com.congnghejava.webbanhang.repository.UserCredentialRepository;

@Service
public class UserCredentialService {
	@Autowired
	UserCredentialRepository userCredentialRepository;

	public UserCredential getCurrentUserCredential(Principal principal) {
		String username = principal.getName();

		UserCredential userCredential = userCredentialRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		return userCredential;
	}
}
