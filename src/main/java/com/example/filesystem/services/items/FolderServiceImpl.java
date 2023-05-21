package com.example.filesystem.services.items;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.filesystem.exceptions.custom.BusinessException;
import com.example.filesystem.models.dao.items.Folder;
import com.example.filesystem.models.dao.items.Item;

@Service
@Transactional
public class FolderServiceImpl extends ItemServiceImpl<Folder> implements FolderService {

	/**
	 * Customize folder insert to take the parent permission group if no permission
	 * group provided and to apply edit access validation on folder parent
	 * 
	 * @param folder
	 */
	@Override
	public Long insert(Folder folder) {
		// get parent item
		Item parent = findById(folder.getParent().getId());
		folder.setParent(parent);

		// get permission group from the parent if not provided
		if (folder.getPermissionGroup().getId() == null) {
			folder.getPermissionGroup().setId(parent.getPermissionGroup().getId());
		}

		// validate user access to parent item
		validateParentEditAccess(parent);

		return super.insert(folder);
	}

	/**
	 * Customize folder update to take the parent permission group if no permission
	 * group provided and to apply edit access validation on folder parent
	 * 
	 * In case of changing the folder permission group, all children must be updated
	 * recursively
	 * 
	 * @param id
	 * @param folder
	 */
	@Override
	public void update(Long id, Folder folder) {
		// get parent item
		Item parent = findById(folder.getParent().getId());
		folder.setParent(parent);

		// get permission group from the parent if not provided
		if (folder.getPermissionGroup().getId() == null) {
			folder.getPermissionGroup().setId(parent.getPermissionGroup().getId());
		}

		// validate user access to parent item
		validateParentEditAccess(parent);

		// TODO update the folder permission group according to the new parent
		// and apply this to all children recursively

		super.update(id, folder);
	}

	/**
	 * Validate the user has edit access to the folder parent
	 * 
	 * @param parent
	 */
	private void validateParentEditAccess(Item parent) {
		if (!permissionService.checkUserEditAccess(parent)) {
			throw new AccessDeniedException(String.format("Sorry! you don't have edit access to the folder (%s)", parent.getName()));
		}
	}

	/**
	 * Validate folder name must be unique in the same folder
	 * 
	 * @param folder
	 */
	@Override
	protected void validateItemDuplication(Folder folder) {
		if (itemRepository.isDuplicatedNameWithParent(folder.getName(), folder.getParent().getId(), folder.getId())) {
			throw new BusinessException("name", String.format("The folder (%s) has already a folder with the same name.", folder.getParent().getName()));
		}
	}
}