package com.example.filesystem.services.permissions;

import java.util.List;

import com.example.filesystem.models.dao.items.Item;
import com.example.filesystem.models.dao.permissions.Permission;

public interface PermissionService {

	public Long insert(Permission permission);

	public void update(Long id, Permission permission);

	public void delete(Long id);

	public List<Permission> getAllPermissions();

	public Permission getPermissionById(Long id);

	public boolean checkUserEditAccess(Item item);

	public boolean checkUserViewAccess(Item item);

	public boolean checkUserDeleteAccess(Item item);

}