package com.example.filesystem.repositories.items;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.filesystem.models.dao.items.Item;

@Repository
public interface ItemRepository<T extends Item> extends JpaRepository<T, Long> {

	@Query(" select case when count(i.id) > 0 then true else false end " +
			" from Item i " +
			" where i.name = :name " +
			" and (:excludedId is null or i.id != :excludedId) ")
	public boolean isDuplicatedName(String name, Long excludedId);

	@Query(" select case when count(i.id) > 0 then true else false end " +
			" from Item i " +
			" where i.name = :name " +
			" and i.parent.id = :parentId " +
			" and (:excludedId is null or i.id != :excludedId) ")
	public boolean isDuplicatedNameWithParent(String name, Long parentId, Long excludedId);

	@Query(value = "" +
			" with recursive cte as ( " +
			"   select p.* from items p " +
			"   where ((:root is null and p.parent_id is null) or (:root is not null and p.id = :root)) " +
			"   and exists (" +
			"     select i.id from permissions i " +
			"     where i.user_email = :userEmail " +
			"     and i.permission_level = :viewPermissionLevel " +
			"     and i.permission_group_id = p.permission_group_id " +
			"   ) " +
			"   union all " +
			"   select c.* from items c " +
			"   inner join cte on c.parent_id = cte.id " +
			"   and exists (" +
			"     select i.id from permissions i " +
			"     where i.user_email = :userEmail " +
			"     and i.permission_level = :viewPermissionLevel " +
			"     and i.permission_group_id = c.permission_group_id " +
			"   ) " +
			" ) select * from cte ", nativeQuery = true)
	public List<? extends Item> getItemMetadata(Long root, String userEmail, String viewPermissionLevel);

}