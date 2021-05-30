package com.congnghejava.webbanhang.controllers;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.congnghejava.webbanhang.payload.response.FileUploadResponse;
import com.congnghejava.webbanhang.payload.response.MessageResponse;
import com.congnghejava.webbanhang.services.FilesStorageService;

@RestController
@RequestMapping("/files")
public class FilesUploadController {
	@Autowired
	FilesStorageService fileStorageService;

	private static final Logger logger = LoggerFactory.getLogger(FilesUploadController.class);

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("files") MultipartFile[] files) {

		try {
			List<String> filenames = new ArrayList<>();

			Arrays.asList(files).stream().forEach(file -> {
				fileStorageService.save(file);
				filenames.add(file.getOriginalFilename());
			});

			String message = "Uploaded the file successfully: " + filenames;
			return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
		} catch (Exception e) {
			String message = "Could not upload the file!";
			return new ResponseEntity<>(new MessageResponse(message), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping
	public ResponseEntity<?> getListFiles() {
		List<FileUploadResponse> filesResponse = fileStorageService.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FilesUploadController.class, "getFile", path.getFileName().toString()).build()
					.toString();
			return new FileUploadResponse(filename, url);
		}).collect(Collectors.toList());

		return new ResponseEntity<>(filesResponse, HttpStatus.OK);
	}

	@GetMapping("/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {

		Resource file = fileStorageService.load(filename);
		String contentType = URLConnection.guessContentTypeFromName(filename);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
}
