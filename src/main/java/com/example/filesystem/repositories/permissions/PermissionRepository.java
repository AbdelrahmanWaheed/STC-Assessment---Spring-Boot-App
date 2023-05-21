package com.example.filesystem.repositories.permissions;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.filesystem.models.dao.permissions.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

	@Query(" select case when count(p.id) > 0 then true else false end " +
			" from Permission p " +
			" where p.userEmail = :userEmail " +
			" and p.permissionGroup.id = :permissionGroupId " +
			" and p.permissionLevel = :permissionLevel " +
			" and (:excludedId is null or p.id != :excludedId) ")
	public boolean isDuplicated(String userEmail, Long permissionGroupId, String permissionLevel, Long excludedId);

	@Query(" select p from Permission p " +
			" where p.userEmail = :userEmail " +
			" and p.permissionGroup.id = :permissionGroupId " +
			" and p.permissionLevel = :permissionLevel ")
	public Optional<Permission> findUserPermissionAccess(String userEmail, Long permissionGroupId, String permissionLevel);

}