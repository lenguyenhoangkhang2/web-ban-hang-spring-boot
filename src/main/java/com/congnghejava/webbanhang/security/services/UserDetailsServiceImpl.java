package com.congnghejava.webbanhang.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.congnghejava.webbanhang.exception.ResourceNotFoundException;
import com.congnghejava.webbanhang.models.UserCredential;
import com.congnghejava.webbanhang.repository.UserCredentialRepository;
import com.congnghejava.webbanhang.security.UserPrincipal;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserCredentialRepository userCredentialRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserCredential userCredential = userCredentialRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));

		return UserPrincipal.create(userCredential);
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		UserCredential userCredential = userCredentialRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		return UserPrincipal.create(userCredential);
	}

}
