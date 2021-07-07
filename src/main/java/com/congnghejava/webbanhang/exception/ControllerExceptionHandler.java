package com.congnghejava.webbanhang.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.mail.MessagingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.congnghejava.webbanhang.payload.response.MessageResponse;
import com.stripe.exception.InvalidRequestException;

@RestControllerAdvice
public class ControllerExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
	public ErrorMessage handleMaxSizeException(MaxUploadSizeExceededException ex) {
		return new ErrorMessage(HttpStatus.EXPECTATION_FAILED.value(), "File is larger " + ex.getMaxUploadSize());
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(ex.getLocalizedMessage()));
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<?> handleAmountExceed() {
		return ResponseEntity.badRequest().body(new MessageResponse("Stripe chỉ nhận thanh toán dưới 100 triệu đồng!"));
	}

	@ExceptionHandler(MailException.class)
	public ResponseEntity<?> handleSendMailException(MailException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new MessageResponse(ex.getLocalizedMessage()));
	}

	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<?> handleSendMailException(MessagingException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new MessageResponse(ex.getLocalizedMessage()));
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException ex) {
		return ResponseEntity.status(HttpStatus.GONE).body(new MessageResponse(ex.getLocalizedMessage()));
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> handleValidationExceptions(BadRequestException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(ex.getLocalizedMessage()));
	}
}
