package com.example.filesystem.controllers.permissions;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filesystem.models.dao.permissions.PermissionGroup;
import com.example.filesystem.models.dto.permissions.PermissionGroupDTO;
import com.example.filesystem.services.permissions.PermissionGroupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("permissions-groups")
@Tag(name = "Permissions")
public class PermissionGroupController {

	@Autowired
	private PermissionGroupService permissionGroupService;

	/**
	 * List all permissions groups
	 * 
	 * @return
	 */
	@GetMapping("")
	@Operation(summary = "list all permissions groups")
	private List<PermissionGroupDTO> getAllPermissionsGroups() {
		return permissionGroupService.getAllPermissionsGroups().stream().map(PermissionGroupDTO::fromDAO)
				.collect(Collectors.toList());
	}

	/**
	 * Get permission group by id
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@Operation(summary = "get permission group by id")
	private PermissionGroupDTO getPermissionGroupById(@PathVariable("id") Long id) {
		PermissionGroup permissionGroup = permissionGroupService.getPermissionGroupById(id);
		return PermissionGroupDTO.fromDAO(permissionGroup);
	}

	/**
	 * Delete permission group by id
	 * 
	 * @param id
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "delete permission group by id")
	private void deletePermissionGroupById(@PathVariable("id") Long id) {
		permissionGroupService.delete(id);
	}

	/**
	 * Insert new permission group
	 * 
	 * @param permissionGroup
	 * @return
	 */
	@PostMapping("")
	@Operation(summary = "insert new permission group")
	private Long insertPermissionGroup(@Valid @RequestBody PermissionGroupDTO permissionGroup) {
		return permissionGroupService.insert(permissionGroup.toDAO());
	}

	/**
	 * Update existing permission group
	 * 
	 * @param permissionGroup
	 * @param id
	 */
	@PutMapping("/{id}")
	@Operation(summary = "update existing permission group")
	private void updatePermissionGroup(@Valid @RequestBody PermissionGroupDTO permissionGroup, @PathVariable("id") Long id) {
		permissionGroupService.update(id, permissionGroup.toDAO());
	}
}