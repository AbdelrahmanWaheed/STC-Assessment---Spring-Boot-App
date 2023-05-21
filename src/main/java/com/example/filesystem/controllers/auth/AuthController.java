package com.example.filesystem.controllers.auth;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filesystem.models.dto.request.LoginCredentialsRequest;
import com.example.filesystem.models.dto.response.auth.TokenResponse;
import com.example.filesystem.services.auth.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("auth")
@Tag(name = "Authentication")
public class AuthController {

	@Autowired
	private AuthService authService;

	/**
	 * Login with email and password to generate JWT token for this account
	 * 
	 * @param credentials
	 * @return
	 */
	@PostMapping("login")
	@Operation(summary = "login with email and password (default password is \"password\")")
	public TokenResponse login(@Valid @RequestBody LoginCredentialsRequest credentials) {
		String token = authService.login(credentials.getEmail(), credentials.getPassword());
		return new TokenResponse(token);
	}

}