package com.example.filesystem.models.dto.items;

import com.example.filesystem.models.dao.items.File;
import com.example.filesystem.models.dao.items.Item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO extends ItemDTO {

	private Long parentId;

	public static FileDTO fromDAO(File file) {
		FileDTO fileDTO = ItemDTO.fromDAO(file, new FileDTO());
		fileDTO.setParentId(file.getParent().getId());
		return fileDTO;
	}

	public File toDAO() {
		File file = super.toDAO(new File());
		file.setParent((new Item()).setId(parentId));
		return file;
	}
}