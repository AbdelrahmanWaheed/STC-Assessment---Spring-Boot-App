package com.example.filesystem.util;

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

	/**
	 * JWT token secret key
	 */
	@Value("${jwt.secret}")
	private String jwtSecret;

	/**
	 * JWT token expiration duration in seconds
	 */
	@Value("${jwt.validity}")
	private Long jwtTokenValidity;

	/**
	 * generate token for user email
	 * 
	 * @param email
	 * @return
	 */
	public String generateToken(String email) {
		return doGenerateToken(email);
	}

	/**
	 * Retrieve email from JWT token
	 * 
	 * @param token
	 * @return
	 */
	public String getEmailFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	/**
	 * Validate token
	 * 
	 * @param token
	 * @return
	 */
	public Boolean validToken(String token) {
		return !isTokenExpired(token);
	}

	/**
	 * Retrieve expiration date from JWT token
	 * 
	 * @param token
	 * @return
	 */
	private Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	/**
	 * Retrieve information from token
	 * 
	 * @param <T>
	 * @param token
	 * @param claimsResolver
	 * @return
	 */
	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Parse the token and get body
	 * 
	 * @param token
	 * @return
	 */
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	}

	/**
	 * Check if the token has expired
	 * 
	 * @param token
	 * @return
	 */
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	/**
	 * Generate JWT token
	 * 
	 * @param subject
	 * @return
	 */
	private String doGenerateToken(String subject) {
		return Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1000))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}
}