package com.congnghejava.webbanhang.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.congnghejava.webbanhang.models.FileUpload;
import com.congnghejava.webbanhang.payload.response.MessageResponse;
import com.congnghejava.webbanhang.services.FilesStorageServiceV2;

@Controller
@RestController("/filesV2")
public class FilesUploadController {
	@Autowired
	FilesStorageServiceV2 fileStorageService;

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			String message = "Uploaded the file successfully: " + file.getOriginalFilename();
			fileStorageService.save(file);
			return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
		} catch (Exception e) {
			String message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return new ResponseEntity<>(new MessageResponse(message), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping
	public ResponseEntity<?> getListFiles() {
		List<FileUpload> files = fileStorageService.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FilesUploadController.class, "getFile", path.getFileName().toString()).build()
					.toString();
			return new FileUpload(filename, url);
		}).collect(Collectors.toList());

		return new ResponseEntity<>(files, HttpStatus.OK);
	}

	@GetMapping("/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = fileStorageService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"").body(file);
	}
}
