package com.example.filesystem.models.dto.items;

import javax.validation.constraints.NotBlank;

import com.example.filesystem.models.dao.items.Item;
import com.example.filesystem.models.dao.permissions.PermissionGroup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ItemDTO {

	protected Long id;

	@NotBlank
	protected String name;

	protected Long permissionGroupId;

	protected static <T extends ItemDTO> T fromDAO(Item item, T itemDTO) {
		itemDTO.setId(item.getId());
		itemDTO.setName(item.getName());
		itemDTO.setPermissionGroupId(item.getPermissionGroup().getId());

		return itemDTO;
	}

	public <T extends Item> T toDAO(T item) {
		item.setId(id);
		item.setName(name);
		item.setPermissionGroup((new PermissionGroup()).setId(permissionGroupId));

		return item;
	}
}