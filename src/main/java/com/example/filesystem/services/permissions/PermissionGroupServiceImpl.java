package com.example.filesystem.services.permissions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.filesystem.exceptions.custom.BusinessException;
import com.example.filesystem.exceptions.custom.ResourceNotFoundException;
import com.example.filesystem.models.dao.permissions.PermissionGroup;
import com.example.filesystem.repositories.permissions.PermissionGroupRepository;

@Service
@Transactional
public class PermissionGroupServiceImpl implements PermissionGroupService {

	@Autowired
	private PermissionGroupRepository permissionGroupRepository;

	/**
	 * Insert permission group
	 * 
	 * @param permissionGroup
	 * @return
	 */
	@Override
	public Long insert(PermissionGroup permissionGroup) {
		validate(permissionGroup);
		permissionGroupRepository.save(permissionGroup);
		return permissionGroup.getId();
	}

	/**
	 * Update permission group
	 * 
	 * @param id
	 * @param permissionGroup
	 * @return
	 */
	@Override
	public void update(Long id, PermissionGroup permissionGroup) {
		if (!permissionGroupRepository.existsById(id)) {
			throw new ResourceNotFoundException(PermissionGroup.class, id);
		}
		permissionGroup.setId(id);
		validate(permissionGroup);
		permissionGroupRepository.save(permissionGroup);
	}

	/**
	 * Validate permission group
	 * 
	 * @param permissionGroup
	 */
	private void validate(PermissionGroup permissionGroup) {
		// validate group name is unique
		if (permissionGroupRepository.isDuplicated(permissionGroup.getGroupName(), permissionGroup.getId())) {
			throw new BusinessException("groupName", String.format("Permission group name (%s) is duplicated.", permissionGroup.getGroupName()));
		}
	}

	/**
	 * Delete permission group by id
	 * 
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		permissionGroupRepository.deleteById(id);
	}

	/**
	 * List all permissions groups
	 */
	@Override
	public List<PermissionGroup> getAllPermissionsGroups() {
		return permissionGroupRepository.findAll();
	}

	/**
	 * Get permission group by id
	 * 
	 * @param id
	 */
	@Override
	public PermissionGroup getPermissionGroupById(Long id) {
		return permissionGroupRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PermissionGroup.class, id));
	}
}