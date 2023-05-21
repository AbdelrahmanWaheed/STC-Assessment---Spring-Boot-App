package com.example.filesystem.controllers.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.filesystem.models.dao.items.File;
import com.example.filesystem.models.dao.items.Folder;
import com.example.filesystem.models.dao.items.Item;
import com.example.filesystem.models.dao.items.Space;
import com.example.filesystem.models.dto.items.FileDTO;
import com.example.filesystem.models.dto.items.FolderDTO;
import com.example.filesystem.models.dto.items.ItemDTO;
import com.example.filesystem.models.dto.items.SpaceDTO;
import com.example.filesystem.services.items.FileService;
import com.example.filesystem.services.items.FolderService;
import com.example.filesystem.services.items.SpaceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("")
@Tag(name = "File System")
public class ItemController {

	@Autowired
	private SpaceService spaceService;

	@Autowired
	private FolderService folderService;

	@Autowired
	private FileService fileService;

	/************* Insertions *************/

	/**
	 * Create new space
	 * 
	 * @param space
	 * @return
	 */
	@PostMapping("create-space")
	@Operation(summary = "create new space")
	private Long insertSpace(@Valid @RequestBody SpaceDTO space) {
		return spaceService.insert(space.toDAO());
	}

	/**
	 * Create new folder
	 * 
	 * @param folder
	 * @return
	 */
	@PostMapping("create-folder")
	@Operation(summary = "create new folder")
	private Long insertFolder(@Valid @RequestBody FolderDTO folder) {
		return folderService.insert(folder.toDAO());
	}

	/**
	 * Create new file and upload the attachment
	 * 
	 * @param bindary
	 * @param name
	 * @param parentId
	 * @return
	 */
	@PostMapping(value = "create-file")
	@Operation(summary = "create new file")
	private Long insertFile(@NotNull @NotBlank @RequestParam("file") MultipartFile bindary,
			@NotBlank @RequestParam("name") String name,
			@NotNull @RequestParam("parentId") Long parentId) {

		FileDTO file = new FileDTO();
		file.setName(name);
		file.setParentId(parentId);
		return fileService.insert(file.toDAO(), bindary);
	}

	/************* Updating *************/

	/**
	 * Update existing space
	 * 
	 * @param space
	 * @param id
	 */
	@PutMapping("update-space/{id}")
	@Operation(summary = "update existing space")
	private void updateSpace(@Valid @RequestBody SpaceDTO space, @PathVariable("id") Long id) {
		spaceService.update(id, space.toDAO());
	}

	/**
	 * Update existing folder
	 * 
	 * @param folder
	 * @param id
	 */
	@PutMapping("update-folder/{id}")
	@Operation(summary = "update existing folder")
	private void updateFolder(@Valid @RequestBody FolderDTO folder, @PathVariable("id") Long id) {
		folderService.update(id, folder.toDAO());
	}

	/**
	 * Update existing file
	 * 
	 * @param id
	 * @param bindary
	 * @param name
	 * @param parentId
	 */
	@PutMapping(value = "update-file/{id}")
	@Operation(summary = "update existing file")
	private void updateFile(@PathVariable("id") Long id,
			@RequestParam(value = "file", required = false) MultipartFile bindary,
			@NotBlank @RequestParam("name") String name,
			@NotNull @RequestParam("parentId") Long parentId) {

		FileDTO file = new FileDTO();
		file.setId(id);
		file.setName(name);
		file.setParentId(parentId);
		fileService.update(id, file.toDAO(), bindary);
	}

	/************* Deletion *************/

	/**
	 * Delete existing space
	 * 
	 * @param id
	 */
	@DeleteMapping("delete-space/{id}")
	@Operation(summary = "delete existing space")
	private void deleteSpace(@PathVariable("id") Long id) {
		spaceService.delete(id);
	}

	/**
	 * Delete existing folder
	 * 
	 * @param id
	 */
	@DeleteMapping("delete-folder/{id}")
	@Operation(summary = "delete existing folder")
	private void deleteFolder(@PathVariable("id") Long id) {
		folderService.delete(id);
	}

	/**
	 * Delete existing file with its attachment
	 * 
	 * @param id
	 */
	@DeleteMapping("delete-file/{id}")
	@Operation(summary = "delete existing file")
	private void deleteFile(@PathVariable("id") Long id) {
		fileService.delete(id);
	}

	/************* Data Fetching *************/

	/**
	 * Download file attachment
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("download-file/{id}")
	@Operation(summary = "download file content")
	public ResponseEntity<byte[]> downloadBinaryFile(@PathVariable Long id) {
		File file = fileService.getFileForDownload(id);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"").body(file.getFile().getContent());
	}

	/**
	 * Display files metadata as a tree (starting from a specific root or starting
	 * from the spaces)
	 * 
	 * @param rootId
	 * @return
	 */
	@GetMapping("file-metadata")
	@Operation(summary = "display files metadata as a tree (starting from a specific root or starting from the spaces)")
	public List<ItemDTO> getFileSystemTree(@RequestParam(name = "root", required = false) Long rootId) {
		// get items in specific hierarchy
		List<? extends Item> itemsList = spaceService.getItemMetadata(rootId);

		// convert Item List to ItemDTO List and construct parent children map
		List<ItemDTO> itemsDTOList = new ArrayList<>();
		Map<Long, List<ItemDTO>> parentMap = new HashMap<>();
		for (Item item : itemsList) {
			if (item instanceof Space) {
				SpaceDTO spaceDTO = SpaceDTO.fromDAO((Space) item);
				itemsDTOList.add(spaceDTO);
			} else if (item instanceof Folder) {
				FolderDTO folderDTO = FolderDTO.fromDAO((Folder) item);
				itemsDTOList.add(folderDTO);
				if (!parentMap.containsKey(folderDTO.getParentId())) {
					parentMap.put(folderDTO.getParentId(), new ArrayList<>());
				}
				parentMap.get(folderDTO.getParentId()).add(folderDTO);
			} else if (item instanceof File) {
				FileDTO fileDTO = FileDTO.fromDAO((File) item);
				itemsDTOList.add(fileDTO);
				if (!parentMap.containsKey(fileDTO.getParentId())) {
					parentMap.put(fileDTO.getParentId(), new ArrayList<>());
				}
				parentMap.get(fileDTO.getParentId()).add(fileDTO);
			}
		}

		// set the children of each parent
		for (ItemDTO itemDTO : itemsDTOList) {
			if (itemDTO instanceof SpaceDTO && parentMap.containsKey(itemDTO.getId())) {
				((SpaceDTO) itemDTO).setChildren(parentMap.get(itemDTO.getId()));
			} else if (itemDTO instanceof FolderDTO && parentMap.containsKey(itemDTO.getId())) {
				((FolderDTO) itemDTO).setChildren(parentMap.get(itemDTO.getId()));
			}
		}

		// collect the roots
		List<ItemDTO> rootsList = new ArrayList<>();
		for (ItemDTO itemDTO : itemsDTOList) {
			if (itemDTO instanceof SpaceDTO || itemDTO.getId().equals(rootId)) {
				rootsList.add(itemDTO);
			}
		}

		// return the roots list
		return rootsList;
	}
}