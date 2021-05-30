package com.congnghejava.webbanhang.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.congnghejava.webbanhang.exception.ResourceNotFoundException;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.models.UserCredential;
import com.congnghejava.webbanhang.models.UserInfo;
import com.congnghejava.webbanhang.payload.request.UserInfoRequest;
import com.congnghejava.webbanhang.payload.response.MessageResponse;
import com.congnghejava.webbanhang.payload.response.UserInfoResponse;
import com.congnghejava.webbanhang.repository.UserCredentialRepository;
import com.congnghejava.webbanhang.repository.UserInfoRepository;
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
	private UserInfoRepository userInfoRepository;

	@GetMapping("/me")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
		UserCredential userCredential = userCredentialRepository.findById(userPrincipal.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

		System.out.println(new UserInfoResponse(userCredential).getAuthorities());

		return ResponseEntity.status(HttpStatus.OK).body(new UserInfoResponse(userCredential));
	}

	@PutMapping("/info")
	public ResponseEntity<?> updateUserInfo(@RequestBody UserInfoRequest userInfoRequest,
			@CurrentUser UserPrincipal userPrincipal) {
		User user = userService.getCurrentUser(userPrincipal);
		UserInfo userInfo = new UserInfo(userInfoRequest.getPhone(), userInfoRequest.getCountry(),
				userInfoRequest.getProvince(), userInfoRequest.getDistrict(), userInfoRequest.getDetail());
		userInfo.setId(user.getUserInfo().getId());
		userInfoRepository.save(userInfo);
		return ResponseEntity.ok(new MessageResponse("Thêm thông tin thành công"));
	}

}
