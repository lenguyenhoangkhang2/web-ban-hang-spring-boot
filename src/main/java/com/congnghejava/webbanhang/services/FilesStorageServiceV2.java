package com.congnghejava.webbanhang.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageServiceV2 {
	public void init();

	public String save(MultipartFile file);

	public Resource load(String filename);

	public void deleteAll();

	public void delete(String filename);

	public Stream<Path> loadAll();
}
