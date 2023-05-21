package com.example.filesystem.services.permissions;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.filesystem.enums.PermissionLevelEnum;
import com.example.filesystem.exceptions.custom.BusinessException;
import com.example.filesystem.exceptions.custom.ResourceNotFoundException;
import com.example.filesystem.models.dao.items.Item;
import com.example.filesystem.models.dao.permissions.Permission;
import com.example.filesystem.repositories.permissions.PermissionRepository;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	/**
	 * Insert permission
	 * 
	 * @param permission
	 */
	@Override
	public Long insert(Permission permission) {
		validate(permission);
		permissionRepository.save(permission);
		return permission.getId();
	}

	/**
	 * Update permission
	 * 
	 * @param id
	 * @param permission
	 */
	@Override
	public void update(Long id, Permission permission) {
		if (!permissionRepository.existsById(id)) {
			throw new ResourceNotFoundException(Permission.class, id);
		}
		permission.setId(id);

		validate(permission);
		permissionRepository.save(permission);
	}

	/**
	 * Validate permission
	 * 
	 * @param permission
	 */
	private void validate(Permission permission) {
		// validate unique access level of users for the group
		if (permissionRepository.isDuplicated(permission.getUserEmail(), permission.getPermissionGroup().getId(), permission.getPermissionLevel(), permission.getId())) {
			throw new BusinessException("permissionLevel", "You already has this access level on this group.");
		}
	}

	/**
	 * Delete permission
	 * 
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		permissionRepository.deleteById(id);
	}

	/**
	 * List all permissions
	 */
	@Override
	public List<Permission> getAllPermissions() {
		return permissionRepository.findAll();
	}

	/**
	 * Get permission by id
	 * 
	 * @param id
	 */
	@Override
	public Permission getPermissionById(Long id) {
		return permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Permission.class, id));
	}

	/**
	 * Check the logged in user has edit access on item
	 * 
	 * @param item
	 */
	@Override
	public boolean checkUserEditAccess(Item item) {
		Optional<Permission> permission = permissionRepository.findUserPermissionAccess(getLoggedInUserEmail(),
				item.getPermissionGroup().getId(), PermissionLevelEnum.EDIT.getValue());
		return permission.isPresent();
	}

	/**
	 * Check the logged in user has view access on item
	 * 
	 * @param item
	 */
	@Override
	public boolean checkUserViewAccess(Item item) {
		Optional<Permission> permission = permissionRepository.findUserPermissionAccess(getLoggedInUserEmail(),
				item.getPermissionGroup().getId(), PermissionLevelEnum.VIEW.getValue());
		return permission.isPresent();
	}

	/**
	 * Check the logged in user has delete access on item
	 * 
	 * @param item
	 */
	@Override
	public boolean checkUserDeleteAccess(Item item) {
		Optional<Permission> permission = permissionRepository.findUserPermissionAccess(getLoggedInUserEmail(),
				item.getPermissionGroup().getId(), PermissionLevelEnum.DELETE.getValue());
		return permission.isPresent();
	}

	/**
	 * Get logged in user email
	 * 
	 * @return
	 */
	private String getLoggedInUserEmail() {
		return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}