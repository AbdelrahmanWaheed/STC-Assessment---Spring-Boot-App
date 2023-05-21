package com.example.filesystem.services.permissions;

import java.util.List;

import com.example.filesystem.models.dao.permissions.PermissionGroup;

public interface PermissionGroupService {

	public Long insert(PermissionGroup permissionGroup);

	public void update(Long id, PermissionGroup permissionGroup);

	public void delete(Long id);

	public List<PermissionGroup> getAllPermissionsGroups();

	public PermissionGroup getPermissionGroupById(Long id);

}