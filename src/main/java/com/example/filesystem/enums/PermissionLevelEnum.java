package com.example.filesystem.enums;

public enum PermissionLevelEnum {

	VIEW("view"), 
	EDIT("edit"), 
	DELETE("delete");

	private String value;

	PermissionLevelEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}