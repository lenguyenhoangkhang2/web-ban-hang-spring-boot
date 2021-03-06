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
			errors.put("confirmPassword", "M???t kh???u x??c nh???n kh??ng ????ng");
		}

		if (userCredentialRepository.existsByEmail(signupRequest.getEmail())) {
			errors.put("email", "Email ???? ????ng k??");
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

		return ResponseEntity.ok(new MessageResponse("????ng k?? th??nh c??ng, truy c???p email ????? x??c nh???n t??i kho???n"));
	}

	@PostMapping("/confirm-user-email")
	public ResponseEntity<?> sendMailConfirm(@RequestBody String email) throws MailException, MessagingException {

		Optional<UserCredential> userCredential = userCredentialRepository.findByEmail(email);

		if (!userCredential.isPresent()) {
			throw new BadRequestException("Kh??ng c?? t??i kho???n v???i email " + email);
		}

		if (userCredential.get().getEmailVerified()) {
			throw new BadRequestException("Email ???? ???????c x??c nh???n tr?????c ????");
		}

		String token = generateConfirmToken(userCredential.get(), emailConfirmSecret, confirmUserEmailTokenSubject);
		String confirmUserEmailUrl = "http://localhost:3000/confirm-user-email/" + token;

		Mail mail = new Mail();

		mail.setFrom(adminEmail);
		mail.setTo(email);
		mail.setSubject("X??c nh???n email ng?????i d??ng Shop C??ng ngh??? Java");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("confirmUserEmailUrl", confirmUserEmailUrl);

		mail.setProps(model);

		emailSenderService.sendEmail(mail, "confirm-user-email");

		return ResponseEntity.ok("???? g???i email x??c nh???n");
	}

	@GetMapping("/confirm-user-email/{token}")
	public ResponseEntity<?> confirmUserEmail(@PathVariable String token) {
		try {
			Claims claims = Jwts.parser().setSigningKey(emailConfirmSecret).parseClaimsJws(token).getBody();

			// Ki???m tra subject c???a token c?? ph???i l?? subject c???a confirm user email
			if (!claims.getSubject().equals(confirmUserEmailTokenSubject)) {
				throw new BadRequestException("Invalid JWT subject");
			}

			Optional<UserCredential> userCredentialOptional = userCredentialRepository
					.findByEmail(claims.get("email", String.class));

			if (!userCredentialOptional.isPresent()) {
				throw new NoSuchElementException("Kh??ng t??m th???y t??i kho???n. Vui l??ng th???c hi???n l???i sau!");
			}

			UserCredential userCredential = userCredentialOptional.get();

			if (userCredential.getEmailVerified()) {
				throw new BadRequestException("Email t??i kho???n ???? ???????c x??c nh???n tr?????c ????");
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
					.body(new EmailVerifyExpiredResponse("Phi??n l??m vi???c ???? h???t h???n", email));
		} catch (UnsupportedJwtException e) {
			throw new BadRequestException("JWT token is unsupported");
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("JWT claims string is empty");
		}

		return ResponseEntity.ok(new MessageResponse("X??c nh???n email th??nh c??ng"));
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> sendMailResetPassword(@RequestBody EmailResetPasswordRequest emailResetPasswordRequest,
			HttpServletRequest request) throws MailException, MessagingException {

		String resetEmail = emailResetPasswordRequest.getEmail();

		Optional<UserCredential> userCredential = userCredentialRepository.findByEmail(resetEmail);
		if (!userCredential.isPresent()) {
			throw new BadRequestException("Kh??ng c?? t??i kho???n v???i email " + resetEmail);
		}

		String token = generateConfirmToken(userCredential.get(), resetPasswordSecret, resetPasswordTokenSubject);
		String resetPasswordUrl = "http://localhost:3000/reset-password/" + token;

		Mail mail = new Mail();

		mail.setFrom(adminEmail);
		mail.setTo(resetEmail);
		mail.setSubject("?????i m???t kh???u Shop C??ng ngh??? Java");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("resetPasswordUrl", resetPasswordUrl);

		mail.setProps(model);

		emailSenderService.sendEmail(mail, "reset-password");

		return ResponseEntity
				.ok(new MessageResponse("Truy c???p email " + resetEmail + " ????? c?? ???????ng d???n thay ?????i m???t kh???u"));
	}

	@PostMapping("/reset-password/{token}")
	public ResponseEntity<?> resetPassword(@PathVariable String token,
			@RequestBody ResetPasswordRequest resetPasswordRequest) {
		String password = resetPasswordRequest.getPassword();
		String passwordConfirm = resetPasswordRequest.getPasswordConfirm();

		if (!password.equals(passwordConfirm)) {
			throw new BadRequestException("M???t kh???u x??c nh???n kh??ng ch??nh x??c");
		}

		try {
			Claims claims = Jwts.parser().setSigningKey(emailConfirmSecret).parseClaimsJws(token).getBody();

			// Ki???m tra subject c???a token c?? ph???i l?? subject c???a reset-password
			if (!claims.getSubject().equals(resetPasswordTokenSubject)) {
				throw new BadRequestException("Invalid JWT subject");
			}

			Optional<UserCredential> userCredentialOptional = userCredentialRepository
					.findByEmail(claims.get("email", String.class));

			if (!userCredentialOptional.isPresent()) {
				throw new NoSuchElementException("Kh??ng t??m th???y t??i kho???n. Vui l??ng th???c hi???n l???i sau!");
			}

			UserCredential userCredential = userCredentialOptional.get();
			userCredential.setPassword(passwordEncoder.encode(password));
			userCredentialRepository.save(userCredential);
		} catch (SignatureException e) {
			throw new BadRequestException("Invalid JWT signature");
		} catch (MalformedJwtException e) {
			throw new BadRequestException("Invalid JWT token");
		} catch (ExpiredJwtException e) {
			throw new TokenExpiredException("Phi??n l??m vi???c ???? h???t h???n, vui l??ng g???i l???i mail m???i");
		} catch (UnsupportedJwtException e) {
			throw new BadRequestException("JWT token is unsupported");
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("JWT claims string is empty");
		}

		return ResponseEntity.ok(new MessageResponse("Thay ?????i m???t kh???u th??nh c??ng"));
	}

	private String generateConfirmToken(UserCredential userCredential, String secret, String subject) {
		Date now = new Date();
		return Jwts.builder().setSubject(subject).claim("email", userCredential.getEmail())
				.setExpiration(new Date((now.getTime() + mailTokenExpirationMsec)))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
}
