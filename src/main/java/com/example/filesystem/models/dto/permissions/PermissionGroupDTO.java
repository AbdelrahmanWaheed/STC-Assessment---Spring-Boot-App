package com.example.filesystem.models.dto.permissions;

import javax.validation.constraints.NotBlank;

import com.example.filesystem.models.dao.permissions.PermissionGroup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionGroupDTO {

	private Long id;

	@NotBlank
	private String groupName;

	public static PermissionGroupDTO fromDAO(PermissionGroup permissionGroup) {
		PermissionGroupDTO permissionGroupDTO = new PermissionGroupDTO();
		permissionGroupDTO.setId(permissionGroup.getId());
		permissionGroupDTO.setGroupName(permissionGroup.getGroupName());

		return permissionGroupDTO;
	}

	public PermissionGroup toDAO() {
		PermissionGroup permissionGroup = new PermissionGroup();
		permissionGroup.setId(id);
		permissionGroup.setGroupName(groupName);

		return permissionGroup;
	}
}