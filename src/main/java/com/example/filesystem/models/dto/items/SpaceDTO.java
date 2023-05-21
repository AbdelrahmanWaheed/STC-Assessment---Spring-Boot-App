package com.example.filesystem.models.dto.items;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.example.filesystem.models.dao.items.Space;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpaceDTO extends ItemDTO {

	private List<? extends ItemDTO> children;

	@Override
	@NotNull
	public Long getPermissionGroupId() {
		return super.getPermissionGroupId();
	}

	public static SpaceDTO fromDAO(Space space) {
		return ItemDTO.fromDAO(space, new SpaceDTO());
	}

	public Space toDAO() {
		return super.toDAO(new Space());
	}
}