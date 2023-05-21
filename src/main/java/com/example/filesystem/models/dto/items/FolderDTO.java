package com.example.filesystem.models.dto.items;

import java.util.List;

import com.example.filesystem.models.dao.items.Folder;
import com.example.filesystem.models.dao.items.Item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FolderDTO extends ItemDTO {

	private Long parentId;

	private List<? extends ItemDTO> children;

	public static FolderDTO fromDAO(Folder folder) {
		FolderDTO folderDTO = ItemDTO.fromDAO(folder, new FolderDTO());
		folderDTO.setParentId(folder.getParent().getId());
		return folderDTO;
	}

	public Folder toDAO() {
		Folder folder = super.toDAO(new Folder());
		folder.setParent((new Item()).setId(parentId));
		return folder;
	}
}