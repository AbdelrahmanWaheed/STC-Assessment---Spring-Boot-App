package com.example.filesystem.models.dto.permissions;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.filesystem.models.dao.permissions.Permission;
import com.example.filesystem.models.dao.permissions.PermissionGroup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionDTO {

	private Long id;

	@NotBlank
	@Email
	private String userEmail;

	@NotBlank
	private String permissionLevel;

	@NotNull
	private Long permissionGroupId;

	public static PermissionDTO fromDAO(Permission permission) {
		PermissionDTO permissionDTO = new PermissionDTO();
		permissionDTO.setId(permission.getId());
		permissionDTO.setUserEmail(permission.getUserEmail());
		permissionDTO.setPermissionGroupId(permission.getPermissionGroup().getId());
		permissionDTO.setPermissionLevel(permission.getPermissionLevel());

		return permissionDTO;
	}

	public Permission toDAO() {
		Permission permission = new Permission();
		permission.setId(id);
		permission.setUserEmail(userEmail);
		permission.setPermissionGroup((new PermissionGroup()).setId(permissionGroupId));
		permission.setPermissionLevel(permissionLevel);

		return permission;
	}
}