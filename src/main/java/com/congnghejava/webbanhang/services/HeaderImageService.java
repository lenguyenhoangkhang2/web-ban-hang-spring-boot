package com.congnghejava.webbanhang.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.congnghejava.webbanhang.models.EHeaderImageType;
import com.congnghejava.webbanhang.models.HeaderImage;
import com.congnghejava.webbanhang.repository.HeaderImageRepository;

@Service
public class HeaderImageService {
	@Autowired
	HeaderImageRepository headerImageRepository;

	public List<HeaderImage> findAll() {
		return headerImageRepository.findAll();
	}

	public List<HeaderImage> findByType(EHeaderImageType type) {
		return headerImageRepository.findByType(type);
	}

	public HeaderImage findById(int id) {
		return headerImageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy ảnh"));
	}

	public List<HeaderImage> findByTypeAndEnable(EHeaderImageType type, Boolean enable) {
		return headerImageRepository.findByTypeAndEnable(type, enable);
	}

	public void delete(HeaderImage headerImage) {
		headerImageRepository.delete(headerImage);
	}

	public HeaderImage save(HeaderImage headerImage) {
		return headerImageRepository.save(headerImage);
	}
}
