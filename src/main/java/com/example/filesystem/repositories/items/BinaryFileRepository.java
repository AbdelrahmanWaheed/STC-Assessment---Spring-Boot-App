package com.example.filesystem.repositories.items;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.filesystem.models.dao.items.BinaryFile;

@Repository
public interface BinaryFileRepository extends JpaRepository<BinaryFile, Long> {

	public BinaryFile findByItemId(Long itemId);

	public int deleteByItemId(Long id);

}