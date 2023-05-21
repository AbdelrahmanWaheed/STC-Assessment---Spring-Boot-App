package com.example.filesystem.services.items;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.filesystem.exceptions.custom.BusinessException;
import com.example.filesystem.models.dao.items.Space;

@Service
@Transactional
public class SpaceServiceImpl extends ItemServiceImpl<Space> implements SpaceService {

	/**
	 * Customize updating a space to update all children recursively if the
	 * permission group is changed
	 * 
	 * @param id
	 * @param space
	 */
	@Override
	public void update(Long id, Space space) {
		// TODO update the space permission group
		// and apply this to all children recursively

		super.update(id, space);
	}

	/**
	 * Validate space name must be unique
	 * 
	 * @param space
	 */
	@Override
	protected void validateItemDuplication(Space space) {
		if (itemRepository.isDuplicatedName(space.getName(), space.getId())) {
			throw new BusinessException("name", "There is already a space with the same name.");
		}
	}
}