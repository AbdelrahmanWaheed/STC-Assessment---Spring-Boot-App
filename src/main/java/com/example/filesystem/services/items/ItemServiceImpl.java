package com.example.filesystem.services.items;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.filesystem.enums.PermissionLevelEnum;
import com.example.filesystem.exceptions.custom.ResourceNotFoundException;
import com.example.filesystem.models.dao.items.Item;
import com.example.filesystem.repositories.items.ItemRepository;
import com.example.filesystem.services.permissions.PermissionService;

@Service
@Transactional
public abstract class ItemServiceImpl<T extends Item> implements ItemService<T> {

	@Autowired
	protected ItemRepository<T> itemRepository;

	@Autowired
	protected PermissionService permissionService;

	/**
	 * Insert item
	 * 
	 * @param item
	 */
	@Override
	public Long insert(T item) {
		validate(item);
		itemRepository.save(item);
		return item.getId();
	}

	/**
	 * Update item
	 * 
	 * @param id
	 * @param item
	 */
	@Override
	public void update(Long id, T item) {
		item.setId(id);
		validate(item);
		itemRepository.save(item);
	}

	/**
	 * Validate item
	 * 
	 * @param item
	 */
	private void validate(T item) {
		validateItemDuplication(item);
	}

	/**
	 * Check user delete access then perform the delete operation
	 * 
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		Item item = findById(id);
		if (!permissionService.checkUserDeleteAccess(item)) {
			throw new AccessDeniedException(String.format("Sorry! you don't have delete access on (%s)", item.getName()));
		}

		itemRepository.deleteById(id);
	}

	/**
	 * Find item by id
	 * 
	 * @param id
	 */
	@Override
	public Item findById(Long id) {
		return itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Item.class, id));
	}

	/**
	 * Get files metadata (hierarchy) for user with view access on their permission
	 * group
	 */
	@Override
	public List<? extends Item> getItemMetadata(Long rootId) {
		return itemRepository.getItemMetadata(rootId, getLoggedInUserEmail(), PermissionLevelEnum.VIEW.getValue());
	}

	/**
	 * Get logged in user email
	 * 
	 * @return
	 */
	private String getLoggedInUserEmail() {
		return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * Validate item duplication (different condition for each type)
	 * 
	 * @param item
	 */
	protected abstract void validateItemDuplication(T item);

}