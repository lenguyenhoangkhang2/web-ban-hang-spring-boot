package com.congnghejava.webbanhang.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.congnghejava.webbanhang.exception.BadRequestException;
import com.congnghejava.webbanhang.exception.TokenExpiredException;
import com.congnghejava.webbanhang.models.AuthProvider;
import com.congnghejava.webbanhang.models.ERole;
import com.congnghejava.webbanhang.models.Mail;
import com.congnghejava.webbanhang.models.Role;
import com.congnghejava.webbanhang.models.UserCredential;
import com.congnghejava.webbanhang.payload.request.EmailResetPasswordRequest;
import com.congnghejava.webbanhang.payload.request.LoginRequest;
import com.congnghejava.webbanhang.payload.request.ResetPasswordRequest;
import com.congnghejava.webbanhang.payload.request.SignupRequest;
import com.congnghejava.webbanhang.payload.response.AuthResponse;
import com.congnghejava.webbanhang.payload.response.EmailVerifyExpiredResponse;
import com.congnghejava.webbanhang.payload.response.MessageResponse;
import com.congnghejava.webbanhang.repository.RoleRepository;
import com.congnghejava.webbanhang.repository.UserCredentialRepository;
import com.congnghejava.webbanhang.security.jwtToken.TokenProvider;
import com.congnghejava.webbanhang.services.EmailSenderService;
import com.congnghejava.webbanhang.utils.UrlImageUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestController
@RequestMapping("/auth")
public class AuthController {
	UrlImageUtils urlImageUtils = new UrlImageUtils();

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

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

	@Autowired
	EmailSenderService emailSenderService;

	@Value("${spring.mail.username}")
	private String adminEmail;

	@Value("${app.auth.mailTokenExpirationMsec}")
	private int mailTokenExpirationMsec;

	@Value("${app.auth.resetPasswordSecret}")
	private String resetPasswordSecret;

	@Value("${app.auth.emailConfirmSecret}")
	private String emailConfirmSecret;

	private String resetPasswordTokenSubject = "resetPasswordToken";

	private String confirmUserEmailTokenSubject = "confirmUserEmailToken";

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		if (!userCredentialRepository.existsByEmail(loginRequest.getEmail())) {
			throw new BadRequestException("Not found email");
		}
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = tokenProvider.createToken(authentication);

		return ResponseEntity.ok(new AuthResponse(token));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest)
			throws MailException, MessagingException {

		Map<String, String> errors = new HashMap<>();
		if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
			errors.put("confirmPassword", "Mật khẩu xác nhận không đúng");
		}

		if (userCredentialRepository.existsByEmail(signupRequest.getEmail())) {
			errors.put("email", "Email đã đăng ký");
		}

		if (errors.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}

		String name = signupRequest.getName();
		UserCredential user = new UserCredential(signupRequest.getEmail(),
				passwordEncoder.encode(signupRequest.getPassword()), AuthProvider.local);

		Role roleUser = RoleRepository.findByName(ERole.ROLE_USER).get();
		user.setRoles(new HashSet<>(Arrays.asList(roleUser)));
		user.getUser().setName(name);
		user.getUser().setAvatarUrl(urlImageUtils.buildPathWithName("default-avatar.png"));

		userCredentialRepository.save(user);

		sendMailConfirm(signupRequest.getEmail());

		return ResponseEntity.ok(new MessageResponse("Đăng ký thành công, truy cập email để xác nhận tài khoản"));
	}

	@PostMapping("/confirm-user-email")
	public ResponseEntity<?> sendMailConfirm(@RequestBody String email) throws MailException, MessagingException {

		Optional<UserCredential> userCredential = userCredentialRepository.findByEmail(email);

		if (!userCredential.isPresent()) {
			throw new BadRequestException("Không có tài khoản với email " + email);
		}

		if (userCredential.get().getEmailVerified()) {
			throw new BadRequestException("Email đã được xác nhận trước đó");
		}

		String token = generateConfirmToken(userCredential.get(), emailConfirmSecret, confirmUserEmailTokenSubject);
		String confirmUserEmailUrl = "http://localhost:3000/confirm-user-email/" + token;

		Mail mail = new Mail();

		mail.setFrom(adminEmail);
		mail.setTo(email);
		mail.setSubject("Xác nhận email người dùng Shop Công nghệ Java");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("confirmUserEmailUrl", confirmUserEmailUrl);

		mail.setProps(model);

		emailSenderService.sendEmail(mail, "confirm-user-email");

		return ResponseEntity.ok("Đã gửi email xác nhận");
	}

	@GetMapping("/confirm-user-email/{token}")
	public ResponseEntity<?> confirmUserEmail(@PathVariable String token) {
		try {
			Claims claims = Jwts.parser().setSigningKey(emailConfirmSecret).parseClaimsJws(token).getBody();

			// Kiểm tra subject của token có phải là subject của confirm user email
			if (!claims.getSubject().equals(confirmUserEmailTokenSubject)) {
				throw new BadRequestException("Invalid JWT subject");
			}

			Optional<UserCredential> userCredentialOptional = userCredentialRepository
					.findByEmail(claims.get("email", String.class));

			if (!userCredentialOptional.isPresent()) {
				throw new NoSuchElementException("Không tìm thấy tài khoản. Vui lòng thực hiện lại sau!");
			}

			UserCredential userCredential = userCredentialOptional.get();

			if (userCredential.getEmailVerified()) {
				throw new BadRequestException("Email tài khoản đã được xác nhận trước đó");
			}

			userCredential.setEmailVerified(true);
			userCredentialRepository.save(userCredential);
		} catch (SignatureException e) {
			throw new BadRequestException("Invalid JWT signature");
		} catch (MalformedJwtException e) {
			throw new BadRequestException("Invalid JWT token");
		} catch (ExpiredJwtException e) {
			Claims claims = e.getClaims();
			if (!claims.getSubject().equals(confirmUserEmailTokenSubject)) {
				throw new BadRequestException("Invalid JWT subject");
			}

			String email = claims.get("email", String.class);

			return ResponseEntity.status(HttpStatus.GONE)
					.body(new EmailVerifyExpiredResponse("Phiên làm việc đã hết hạn", email));
		} catch (UnsupportedJwtException e) {
			throw new BadRequestException("JWT token is unsupported");
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("JWT claims string is empty");
		}

		return ResponseEntity.ok(new MessageResponse("Xác nhận email thành công"));
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> sendMailResetPassword(@RequestBody EmailResetPasswordRequest emailResetPasswordRequest,
			HttpServletRequest request) throws MailException, MessagingException {

		String resetEmail = emailResetPasswordRequest.getEmail();

		Optional<UserCredential> userCredential = userCredentialRepository.findByEmail(resetEmail);
		if (!userCredential.isPresent()) {
			throw new BadRequestException("Không có tài khoản với email " + resetEmail);
		}

		String token = generateConfirmToken(userCredential.get(), resetPasswordSecret, resetPasswordTokenSubject);
		String resetPasswordUrl = "http://localhost:3000/reset-password/" + token;

		Mail mail = new Mail();

		mail.setFrom(adminEmail);
		mail.setTo(resetEmail);
		mail.setSubject("Đổi mật khẩu Shop Công nghệ Java");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("resetPasswordUrl", resetPasswordUrl);

		mail.setProps(model);

		emailSenderService.sendEmail(mail, "reset-password");

		return ResponseEntity
				.ok(new MessageResponse("Truy cập email " + resetEmail + " để có đường dẫn thay đổi mật khẩu"));
	}

	@PostMapping("/reset-password/{token}")
	public ResponseEntity<?> resetPassword(@PathVariable String token,
			@RequestBody ResetPasswordRequest resetPasswordRequest) {
		String password = resetPasswordRequest.getPassword();
		String passwordConfirm = resetPasswordRequest.getPasswordConfirm();

		if (!password.equals(passwordConfirm)) {
			throw new BadRequestException("Mật khẩu xác nhận không chính xác");
		}

		try {
			Claims claims = Jwts.parser().setSigningKey(emailConfirmSecret).parseClaimsJws(token).getBody();

			// Kiểm tra subject của token có phải là subject của reset-password
			if (!claims.getSubject().equals(resetPasswordTokenSubject)) {
				throw new BadRequestException("Invalid JWT subject");
			}

			Optional<UserCredential> userCredentialOptional = userCredentialRepository
					.findByEmail(claims.get("email", String.class));

			if (!userCredentialOptional.isPresent()) {
				throw new NoSuchElementException("Không tìm thấy tài khoản. Vui lòng thực hiện lại sau!");
			}

			UserCredential userCredential = userCredentialOptional.get();
			userCredential.setPassword(passwordEncoder.encode(password));
			userCredentialRepository.save(userCredential);
		} catch (SignatureException e) {
			throw new BadRequestException("Invalid JWT signature");
		} catch (MalformedJwtException e) {
			throw new BadRequestException("Invalid JWT token");
		} catch (ExpiredJwtException e) {
			throw new TokenExpiredException("Phiên làm việc đã hết hạn, vui lòng gửi lại mail mới");
		} catch (UnsupportedJwtException e) {
			throw new BadRequestException("JWT token is unsupported");
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("JWT claims string is empty");
		}

		return ResponseEntity.ok(new MessageResponse("Thay đổi mật khẩu thành công"));
	}

	private String generateConfirmToken(UserCredential userCredential, String secret, String subject) {
		Date now = new Date();
		return Jwts.builder().setSubject(subject).claim("email", userCredential.getEmail())
				.setExpiration(new Date((now.getTime() + mailTokenExpirationMsec)))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
}
