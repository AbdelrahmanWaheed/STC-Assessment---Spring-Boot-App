package com.example.filesystem.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.filesystem.util.JwtTokenUtil;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${auth.password}")
	private String authenticationPassword;

	/**
	 * Generate the token after successful authentication otherwise throw bad
	 * credentials exception
	 * 
	 * @param email
	 * @param password
	 * @throws BadCredentialsException
	 * @return
	 */
	@Override
	public String login(String email, String password) {
		if (!password.equals(authenticationPassword)) {
			throw new BadCredentialsException("Invalid login attempt");
		}
		return jwtTokenUtil.generateToken(email);
	}

	/**
	 * Get the email from token if this token is valid otherwise return null
	 * 
	 * @param authorizationHeader
	 * @return
	 */
	@Override
	public String authenticate(String authorizationHeader) {
		if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
			return null;
		}

		// Get token and validate
		final String token = authorizationHeader.split(" ")[1].trim();
		if (!jwtTokenUtil.validToken(token)) {
			return null;
		}

		// get user email from token
		return jwtTokenUtil.getEmailFromToken(token);
	}
}