package com.congnghejava.webbanhang.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.congnghejava.webbanhang.models.EProductImageTypeDisplay;
import com.congnghejava.webbanhang.models.Product;

public interface FilesStorageService {
	public void init();

	public Resource load(String filename);

	public void deleteAll();

	public void delete(String filename);

	public Stream<Path> loadAll();

	public String save(MultipartFile file);

	public void saveImageForProduct(MultipartFile file, Product product, EProductImageTypeDisplay type);
}
