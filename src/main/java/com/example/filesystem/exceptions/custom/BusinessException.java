package com.example.filesystem.exceptions.custom;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

	private String field;

	public BusinessException(String field, String message) {
		super(message);
		this.field = field;
	}
}