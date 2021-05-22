package com.congnghejava.webbanhang.payload.response;

public class FileUploadResponse {
	private String name;
	private String url;

	public FileUploadResponse(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
