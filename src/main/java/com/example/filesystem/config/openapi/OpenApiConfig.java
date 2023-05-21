package com.example.filesystem.config.openapi;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI configureOpenAPI() {
		return new OpenAPI().components(new Components()
				.addSecuritySchemes("Bearer Authentication", new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
						.in(SecurityScheme.In.HEADER).name("Authorization")))
				.info(new Info().title("File System API").version("3.0"))
				.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication", Arrays.asList("read", "write")));
	}
}