package com.congnghejava.webbanhang.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.congnghejava.webbanhang.exception.ResourceNotFoundException;
import com.congnghejava.webbanhang.models.ERole;
import com.congnghejava.webbanhang.models.Role;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.models.UserContact;
import com.congnghejava.webbanhang.models.UserCredential;
import com.congnghejava.webbanhang.payload.request.UserInfoRequest;
import com.congnghejava.webbanhang.payload.response.MessageResponse;
import com.congnghejava.webbanhang.payload.response.UserInfoResponse;
import com.congnghejava.webbanhang.repository.RoleRepository;
import com.congnghejava.webbanhang.repository.UserContactRepository;
import com.congnghejava.webbanhang.repository.UserCredentialRepository;
import com.congnghejava.webbanhang.security.CurrentUser;
import com.congnghejava.webbanhang.security.UserPrincipal;
import com.congnghejava.webbanhang.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserCredentialRepository userCredentialRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserContactRepository userContactRepository;

	@Autowired
	private RoleRepository roleRepository;

	@GetMapping("/me")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
		UserCredential userCredential = userCredentialRepository.findById(userPrincipal.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
		return ResponseEntity.status(HttpStatus.OK).body(new UserInfoResponse(userCredential));
	}

	@PutMapping("/info")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> updateUserInfo(@RequestBody UserInfoRequest userInfoRequest,
			@CurrentUser UserPrincipal userPrincipal) {
		User user = userService.getCurrentUser(userPrincipal);
		UserContact userContact = new UserContact(userInfoRequest.getPhone(), userInfoRequest.getCountry(),
				userInfoRequest.getProvince(), userInfoRequest.getDistrict(), userInfoRequest.getDetail());
		userContact.setId(user.getUserInfo().getId());
		userContactRepository.save(userContact);
		return ResponseEntity.ok(new MessageResponse("Thêm thông tin thành công"));
	}

	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getAllUser(
			@PageableDefault(page = 0, size = 2, sort = { "id" }, direction = Direction.ASC) Pageable page) {

		Page<UserCredential> pageUser = userCredentialRepository.findAll(page);
		List<UserInfoResponse> listUser = pageUser.getContent().stream().map(i -> new UserInfoResponse(i))
				.collect(Collectors.toList());
		return ResponseEntity.ok(new PageImpl<>(listUser, page, pageUser.getTotalElements()));
	}

	@PostMapping("/addAdminRole")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> addRoleAdmin(@RequestBody Long userId) {
		UserCredential userCredential = userCredentialRepository.findById(userId).get();

		Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
		userCredential.getRoles().add(adminRole);

		userCredentialRepository.save(userCredential);
		return ResponseEntity.ok(new MessageResponse("Thêm quyền admin thành công"));
	}
}
