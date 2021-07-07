package com.congnghejava.webbanhang.payload.response;

import java.text.SimpleDateFormat;

import com.congnghejava.webbanhang.models.HeaderImage;
import com.congnghejava.webbanhang.utils.UrlImageUtils;

public class HeaderImageResponse {
	UrlImageUtils urlImageUtils = new UrlImageUtils();

	private int id;

	private String title;

	private String url;

	private String linkTo;

	private String type;

	private Boolean enable;

	private String createdOn;

	public HeaderImageResponse(HeaderImage headerImage) {
		id = headerImage.getId();
		title = headerImage.getTitle();
		url = urlImageUtils.buildPathWithName(headerImage.getFileUploadName());
		linkTo = headerImage.getLinkTo();
		type = headerImage.getType().toString();
		enable = headerImage.getEnable();
		createdOn = new SimpleDateFormat("dd/MM/yyyy").format(headerImage.getCreateDate());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLinkTo() {
		return linkTo;
	}

	public void setLinkTo(String linkTo) {
		this.linkTo = linkTo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

}
