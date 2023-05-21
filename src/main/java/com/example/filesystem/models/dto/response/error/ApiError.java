package com.example.filesystem.models.dto.response.error;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;

@Getter
public class ApiError {

	private LocalDateTime timestamp;
	private HttpStatus status;
	private String message;
	private List<Violation> errors;

	public static ResponseEntity<Object> buildResponseEntity(LocalDateTime timestamp, HttpStatus status, String message,
			List<Violation> errors) {
		return ResponseEntity.status(status).body(new ApiError(timestamp, status, message, errors));
	}

	public static ResponseEntity<Object> buildResponseEntity(LocalDateTime timestamp, HttpStatus status,
			String message) {
		return ResponseEntity.status(status).body(new ApiError(timestamp, status, message, null));
	}

	private ApiError(LocalDateTime timestamp, HttpStatus status, String message, List<Violation> errors) {
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
		this.errors = errors;
	}
}