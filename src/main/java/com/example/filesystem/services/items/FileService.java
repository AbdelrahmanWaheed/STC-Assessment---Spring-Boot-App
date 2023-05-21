package com.example.filesystem.services.items;

import org.springframework.web.multipart.MultipartFile;

import com.example.filesystem.models.dao.items.File;

public interface FileService extends ItemService<File> {

	public Long insert(File file, MultipartFile binary);

	public void update(Long id, File file, MultipartFile binary);

	public File getFileForDownload(Long id);

}