package com.example.filesystem.services.items;

import java.util.List;

import com.example.filesystem.models.dao.items.Item;

public interface ItemService<T extends Item> {

	public Long insert(T item);

	public void update(Long id, T item);

	public void delete(Long id);

	public Item findById(Long id);

	public List<? extends Item> getItemMetadata(Long rootId);

}