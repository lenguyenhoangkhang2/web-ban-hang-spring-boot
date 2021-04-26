package com.congnghejava.webbanhang.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ControllerExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorMessage handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		List<String> errors = new ArrayList<String>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		return new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), errors);

	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
	public ErrorMessage handleMaxSizeException(MaxUploadSizeExceededException ex) {
		return new ErrorMessage(HttpStatus.EXPECTATION_FAILED.value(), ex.getMessage(),
				"File is larger " + ex.getMaxUploadSize());
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
}
