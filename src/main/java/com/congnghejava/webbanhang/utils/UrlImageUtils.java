package com.congnghejava.webbanhang.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class UrlImageUtils {
	public UrlImageUtils() {
		// TODO Auto-generated constructor stub
	}

	public String buildPathWithName(String fileName) {
		if (fileName.isEmpty()) {
			return null;
		}
		return ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/").path(fileName).toUriString();
	}
}
