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

import com.example.filesystem.models.dao.permissions.Permission;
import com.example.filesystem.models.dto.permissions.PermissionDTO;
import com.example.filesystem.services.permissions.PermissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("permissions")
@Tag(name = "Permissions")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;

	/**
	 * List all permissions
	 * 
	 * @return
	 */
	@GetMapping("")
	@Operation(summary = "list all permissions")
	private List<PermissionDTO> getAllPermissions() {
		return permissionService.getAllPermissions().stream().map(PermissionDTO::fromDAO)
				.collect(Collectors.toList());
	}

	/**
	 * Get permission by id
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@Operation(summary = "get permission by id")
	private PermissionDTO getPermissionById(@PathVariable("id") Long id) {
		Permission permission = permissionService.getPermissionById(id);
		return PermissionDTO.fromDAO(permission);
	}

	/**
	 * Delete permission by id
	 * 
	 * @param id
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "delete permission by id")
	private void deletePermissionById(@PathVariable("id") Long id) {
		permissionService.delete(id);
	}

	/**
	 * Insert new permission
	 * 
	 * @param permission
	 * @return
	 */
	@PostMapping("")
	@Operation(summary = "insert new permission")
	private Long insertPermission(@Valid @RequestBody PermissionDTO permission) {
		return permissionService.insert(permission.toDAO());
	}

	/**
	 * Update existing permission
	 * 
	 * @param permission
	 * @param id
	 */
	@PutMapping("/{id}")
	@Operation(summary = "update existing permission")
	private void updatePermission(@Valid @RequestBody PermissionDTO permission, @PathVariable("id") Long id) {
		permissionService.update(id, permission.toDAO());
	}
}