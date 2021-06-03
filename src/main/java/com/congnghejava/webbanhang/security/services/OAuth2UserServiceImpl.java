package com.congnghejava.webbanhang.security.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.congnghejava.webbanhang.exception.OAuth2AuthenticationProcessingException;
import com.congnghejava.webbanhang.exception.ResourceNotFoundException;
import com.congnghejava.webbanhang.models.AuthProvider;
import com.congnghejava.webbanhang.models.ERole;
import com.congnghejava.webbanhang.models.Role;
import com.congnghejava.webbanhang.models.UserCredential;
import com.congnghejava.webbanhang.repository.RoleRepository;
import com.congnghejava.webbanhang.repository.UserCredentialRepository;
import com.congnghejava.webbanhang.security.UserPrincipal;
import com.congnghejava.webbanhang.security.oauth2.user.OAuth2UserInfo;
import com.congnghejava.webbanhang.security.oauth2.user.OAuth2UserInfoFactory;

@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
	@Autowired
	private UserCredentialRepository userCredentialRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Logger logger = LoggerFactory.getLogger(OAuth2UserServiceImpl.class);

	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

		try {
			return processOAuth2User(oAuth2UserRequest, oAuth2User);
		} catch (AuthenticationException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
		}

	}

	private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
		logger.trace("oAuth2User is" + oAuth2User);

		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getAuth2UserInfo(
				oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

		if (ObjectUtils.isEmpty(oAuth2UserInfo.getEmail())) {
			throw new OAuth2AuthenticationProcessingException("Email not found from provider");
		}

		Optional<UserCredential> userOptional = userCredentialRepository.findByEmail(oAuth2UserInfo.getEmail());
		UserCredential userCredential;

		if (userOptional.isPresent()) {
			userCredential = userOptional.get();
			if (!userCredential.getProvider()
					.equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
				throw new OAuth2AuthenticationProcessingException(
						"You're signed with your " + userCredential.getEmail() + " email!");
			}
			userCredential = updateExistingUser(userCredential, oAuth2UserInfo);
		} else {
			userCredential = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
		}
		return UserPrincipal.create(userCredential, oAuth2User.getAttributes());
	}

	private UserCredential registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
		UserCredential userCredential = new UserCredential();

		userCredential.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
		userCredential.setEmail(oAuth2UserInfo.getEmail());
		userCredential.setEmailVerified(true);
		userCredential.getUser().setName(oAuth2UserInfo.getName());
		userCredential.setProviderId(oAuth2UserInfo.getId());
		userCredential.getUser().setAvatarUrl(oAuth2UserInfo.getImageUrl());

		String password = generatePassayPassword();
		userCredential.setPassword(passwordEncoder.encode(password));

		logger.info("(OAuth2UserServiceImpl) Generated password is : " + password);

		Set<Role> roles = new HashSet<>();

		roles.add(roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new ResourceNotFoundException("Role", "ERole", "ROLE_USER")));

		userCredential.setRoles(roles);

		return userCredentialRepository.save(userCredential);
	}

	private UserCredential updateExistingUser(UserCredential existingUserCredential, OAuth2UserInfo oAuth2UserInfo) {
		existingUserCredential.getUser().setName(oAuth2UserInfo.getName());
		existingUserCredential.getUser().setAvatarUrl(oAuth2UserInfo.getImageUrl());

		return userCredentialRepository.save(existingUserCredential);
	}

	private String generatePassayPassword() {
		PasswordGenerator gen = new PasswordGenerator();

		CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 2);
		CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase, 2);
		CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit, 2);

		String password = gen.generatePassword(10, lowerCaseRule, upperCaseRule, digitRule);

		return password;
	}
}
