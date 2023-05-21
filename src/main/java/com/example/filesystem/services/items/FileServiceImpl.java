package com.example.filesystem.services.items;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.filesystem.exceptions.custom.BusinessException;
import com.example.filesystem.models.dao.items.BinaryFile;
import com.example.filesystem.models.dao.items.File;
import com.example.filesystem.models.dao.items.Item;
import com.example.filesystem.repositories.items.BinaryFileRepository;

@Service
@Transactional
public class FileServiceImpl extends ItemServiceImpl<File> implements FileService {

	@Autowired
	private BinaryFileRepository binaryFileRepository;

	/**
	 * Customize file insert to take the parent permission group if no permission
	 * group provided and to save the attachment uploaded with that file.
	 * 
	 * Check on user edit access on the file parent before saving
	 * 
	 * @param file
	 * @param binary
	 */
	@Override
	public Long insert(File file, MultipartFile binary) {
		try {
			// prepare the binary file
			BinaryFile binaryFile = new BinaryFile();
			binaryFile.setContent(binary.getBytes());
			binaryFile.setItem(file);
			file.setFile(binaryFile);

			// get parent item
			Item parent = findById(file.getParent().getId());
			file.setParent(parent);

			// get permission group from the parent if not provided
			if (file.getPermissionGroup().getId() == null) {
				file.getPermissionGroup().setId(parent.getPermissionGroup().getId());
			}

			// validate user access to parent item
			validateParentEditAccess(parent);

			Long id = super.insert(file);
			binaryFileRepository.save(binaryFile);
			return id;
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException("file", "Error uploading the attached file.");
		}
	}

	/**
	 * Customize file update to take the parent permission group if no permission
	 * group provided and to update the attachment uploaded with that file.
	 * 
	 * Check on user edit access on the file parent before updating
	 * 
	 * @param id
	 * @param file
	 * @param binary
	 */
	@Override
	public void update(Long id, File file, MultipartFile binary) {
		try {
			// get parent item
			Item parent = findById(file.getParent().getId());
			file.setParent(parent);

			// get permission group from the parent if not provided
			if (file.getPermissionGroup().getId() == null) {
				file.getPermissionGroup().setId(parent.getPermissionGroup().getId());
			}

			// validate user access to parent item
			validateParentEditAccess(parent);

			// update file binary if new one is uploaded
			if (binary != null) {
				BinaryFile binaryFile = binaryFileRepository.findByItemId(id);
				binaryFile.setContent(binary.getBytes());
				binaryFile.setItem(file);
				file.setFile(binaryFile);

				binaryFileRepository.save(file.getFile());
			}

			super.update(id, file);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException("file", "Error uploading the attached file.");
		}
	}

	/**
	 * Customize file delete to delete also the attachment
	 */
	@Override
	public void delete(Long id) {
		binaryFileRepository.deleteByItemId(id);
		super.delete(id);
	}

	/**
	 * Check the user has view access on the file then get it for download
	 * 
	 * @param id
	 */
	@Override
	public File getFileForDownload(Long id) {
		Item item = findById(id);
		if (!(item instanceof File)) {
			throw new AccessDeniedException("This is not a valid file for download.");
		}

		File file = (File) item;

		// validate view access before download
		validateFileViewAccess(file);

		return file;
	}

	/**
	 * Validate user has edit access on file parent
	 * 
	 * @param parent
	 */
	private void validateParentEditAccess(Item parent) {
		if (!permissionService.checkUserEditAccess(parent)) {
			throw new AccessDeniedException(String.format("Sorry! you don't have edit access to the folder (%s)", parent.getName()));
		}
	}

	/**
	 * Validate user has view access on file
	 * 
	 * @param parent
	 */
	private void validateFileViewAccess(File file) {
		if (!permissionService.checkUserViewAccess(file)) {
			throw new AccessDeniedException(String.format("Sorry! you don't have view access to download the file (%s)", file.getName()));
		}
	}

	/**
	 * Validate file name must be unique in the same folder
	 * 
	 * @param parent
	 */
	@Override
	protected void validateItemDuplication(File file) {
		if (itemRepository.isDuplicatedNameWithParent(file.getName(), file.getParent().getId(), file.getId())) {
			throw new BusinessException("name", String.format("The folder (%s) has already a file with the same name.", file.getParent().getName()));
		}
	}
}