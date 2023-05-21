package com.example.filesystem.services.auth;

public interface AuthService {

	public String login(String email, String password);

	public String authenticate(String token);

}