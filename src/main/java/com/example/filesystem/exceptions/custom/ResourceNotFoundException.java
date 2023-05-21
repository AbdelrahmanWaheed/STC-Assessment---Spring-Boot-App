package com.example.filesystem.exceptions.custom;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(Class<?> classRef, Long id) {
		super(String.format("The record of class (%s) with id (%d) is not found.", classRef.getSimpleName(), id));
	}
}