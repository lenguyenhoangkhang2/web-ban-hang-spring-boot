package com.congnghejava.webbanhang.payload.response;

import com.congnghejava.webbanhang.models.ProductImage;
import com.congnghejava.webbanhang.utils.UrlImageUtils;

public class ProductImageResponse {

	UrlImageUtils urlImageUtils = new UrlImageUtils();

	private String url;
	private String type;

	public ProductImageResponse(ProductImage productImage) {
		this.url = urlImageUtils.buildPathWithName(productImage.getName());
		this.type = productImage.getType().toString();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
