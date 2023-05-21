package com.example.filesystem.models.dto.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Violation {

	private String field;
	private String message;

}