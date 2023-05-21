package com.example.filesystem.models.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginCredentialsRequest {

	@NotBlank
	private String email;

	@NotBlank
	private String password;

}