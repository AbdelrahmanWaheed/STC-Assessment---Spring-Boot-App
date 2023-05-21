package com.example.filesystem.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.filesystem.config.security.filters.JwtTokenFilter;

@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtTokenFilter jwtTokenFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/auth/**").permitAll()	// permit the auth routes (login)
				.antMatchers("/swagger-ui/**", "/v3/api-docs**", "/v3/api-docs/**").permitAll()		// permit openapi routes
				.anyRequest().authenticated()			// authorize all other requests
				.and()
				.csrf().disable()						// disable csrf for webservices
				.exceptionHandling()
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

		// Add a filter to validate the tokens with every request
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}