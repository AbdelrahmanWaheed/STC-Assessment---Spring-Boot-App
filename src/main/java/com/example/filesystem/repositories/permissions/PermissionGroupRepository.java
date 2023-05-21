package com.example.filesystem.repositories.permissions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.filesystem.models.dao.permissions.PermissionGroup;

@Repository
public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Long> {

	@Query(" select case when count(pg.id) > 0 then true else false end " + 
			" from PermissionGroup pg " + 
			" where pg.groupName = :groupName " +
			" and (:excludedId is null or pg.id != :excludedId) ")
	public boolean isDuplicated(String groupName, Long excludedId);

}