package com.example.filesystem.exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.filesystem.exceptions.custom.BusinessException;
import com.example.filesystem.exceptions.custom.ResourceNotFoundException;
import com.example.filesystem.models.dto.response.error.ApiError;
import com.example.filesystem.models.dto.response.error.Violation;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Violation> errors = new ArrayList<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			errors.add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
		}
		return ApiError.buildResponseEntity(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Validation Errors", errors);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
		return ApiError.buildResponseEntity(LocalDateTime.now(), HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		List<Violation> errors = new ArrayList<>();
		errors.add(new Violation(ex.getParameterName(), "parameter is missing"));

		return ApiError.buildResponseEntity(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Missing Parameters", errors);
	}

	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Violation> errors = new ArrayList<>();
		errors.add(new Violation(ex.getRequestPartName(), "file is missing"));

		return ApiError.buildResponseEntity(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Missing Parameters", errors);
	}

	@ExceptionHandler({ AuthenticationException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
		return ApiError.buildResponseEntity(LocalDateTime.now(), HttpStatus.UNAUTHORIZED, ex.getMessage());
	}

	@ExceptionHandler({ AccessDeniedException.class })
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		return ApiError.buildResponseEntity(LocalDateTime.now(), HttpStatus.FORBIDDEN, ex.getMessage());
	}

	@ExceptionHandler({ BusinessException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
		List<Violation> errors = new ArrayList<>();
		errors.add(new Violation(ex.getField(), ex.getMessage()));

		return ApiError.buildResponseEntity(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Validation Errors", errors);
	}

	@ExceptionHandler({ ResourceNotFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		return ApiError.buildResponseEntity(LocalDateTime.now(), HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		return ApiError.buildResponseEntity(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Invalid request parameters");
	}

	@ExceptionHandler({ Exception.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		ex.printStackTrace();
		return ApiError.buildResponseEntity(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}
}