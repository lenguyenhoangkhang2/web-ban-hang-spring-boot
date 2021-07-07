package com.congnghejava.webbanhang.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.congnghejava.webbanhang.models.UserCredential;

public class UserPrincipal implements OAuth2User, UserDetails {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(UserPrincipal.class);

	private Long id;
	private String email;
	private String password;
	private Boolean emailVerified;
	private Collection<? extends GrantedAuthority> authorities;
	private Map<String, Object> attributes;

	public UserPrincipal(Long id, String email, String password, Boolean emailVerified,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.email = email;
		this.emailVerified = emailVerified;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserPrincipal create(UserCredential userCredential) {
		List<GrantedAuthority> authorities = userCredential.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

		UserPrincipal userPrincipal = new UserPrincipal(userCredential.getId(), userCredential.getEmail(),
				userCredential.getPassword(), userCredential.getEmailVerified(), authorities);

		logger.debug("Create UserPrincipal from UserCredential: " + userPrincipal.toString());

		return userPrincipal;
	}

	public static UserPrincipal create(UserCredential userCredential, Map<String, Object> attributes) {
		UserPrincipal userPrincipal = UserPrincipal.create(userCredential);
		userPrincipal.setAttributes(attributes);

		logger.debug("Create UserPrincipal from UserCredential and attributes: " + userPrincipal.toString());
		return userPrincipal;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getName() {
		return String.valueOf(id);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return emailVerified;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "UserPrincipal [id=" + id + ", email=" + email + ", password=" + password + ", authorities="
				+ authorities + ", attributes=" + attributes + "]";
	}

}
