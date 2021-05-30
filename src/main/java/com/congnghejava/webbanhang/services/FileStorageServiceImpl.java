package com.congnghejava.webbanhang.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.congnghejava.webbanhang.models.EProductImageTypeDisplay;
import com.congnghejava.webbanhang.models.Product;

@Service
public class FileStorageServiceImpl implements FilesStorageService {

	@Autowired
	ProductImageServiceImpl productImageService;

	private final Path root = Paths.get("uploads");

	@Override
	public void init() {
		try {
			Files.createDirectory(root);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}

	}

	@Override
	public String save(MultipartFile file) {
		UUID uuid = UUID.randomUUID();
		try {
			String fileName = uuid.toString() + "_" + file.getOriginalFilename();

			Files.copy(file.getInputStream(), root.resolve(fileName));
			return fileName;
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	@Override
	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read this file");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error: " + e.getLocalizedMessage());
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(root, 1).filter(path -> !path.equals(root)).map(root::relativize);
		} catch (Exception e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

	@Override
	public void delete(String filename) {
		try {
			Path filePath = root.resolve(filename);
			Files.deleteIfExists(filePath);
		} catch (Exception e) {
			throw new RuntimeException("Error: " + e.getLocalizedMessage());
		}
	}

	@Override
	public void saveImageForProduct(MultipartFile file, Product product, EProductImageTypeDisplay type) {
		String fileName = save(file);
		productImageService.save(product, fileName, type);
	}
}
