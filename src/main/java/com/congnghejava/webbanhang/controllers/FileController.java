package com.congnghejava.webbanhang.controllers;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.congnghejava.webbanhang.models.FileDB;
import com.congnghejava.webbanhang.payload.response.FileResponse;
import com.congnghejava.webbanhang.payload.response.MessageResponse;
import com.congnghejava.webbanhang.services.FileStorageService;
import com.congnghejava.webbanhang.services.UserService;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileController {
	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private UserService userService;

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, Principal principal) {
		String message = "";

		try {
			System.out.println(principal.getName());
			fileStorageService.store(file, userService.getCurrentUser(principal));
			message = "Upload the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			message = "Could not upload file: " + file.getOriginalFilename() + "!";
			// new MessageResponse(message)
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<List<FileResponse>> getAllFile() {
		List<FileResponse> files = fileStorageService.getAllFiles().map(dbFile -> {
			System.out.println(dbFile.getId());
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
					.path(dbFile.getId()).toUriString();

			return new FileResponse(dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	@GetMapping("/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) {
		FileDB fileDB = fileStorageService.getFile(id);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
				.body(fileDB.getData());
	}
}
