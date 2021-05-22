package com.congnghejava.webbanhang.payload.response;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.congnghejava.webbanhang.models.EProductImageTypeDisplay;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.ProductImage;
import com.congnghejava.webbanhang.services.ProductImageServiceImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ProductImageResponse {
	@Autowired
	ProductImageServiceImpl productImageService;

	private String official;
	private String banner;
	private List<String> slider;

	public ProductImageResponse(Product product) {
		Optional<ProductImage> imageOfficialName = productImageService.findFirstByProductAndType(product,
				EProductImageTypeDisplay.Official);

		Optional<ProductImage> imageBannerName = productImageService.findFirstByProductAndType(product,
				EProductImageTypeDisplay.Banner);

		List<ProductImage> slider = productImageService.findByProductAndType(product, EProductImageTypeDisplay.Slider);

		if (imageOfficialName.isPresent()) {
			this.official = buildPathImage(imageOfficialName.get().getName());
		}

		if (imageBannerName.isPresent()) {
			this.banner = buildPathImage(imageBannerName.get().getName());
		}

		this.slider = slider.stream().map(image -> buildPathImage(image.getName())).collect(Collectors.toList());
	}

	public String getOfficial() {
		return official;
	}

	public String getBanner() {
		return banner;
	}

	public List<String> getSlider() {
		return slider;
	}

	public String buildPathImage(String fileName) {
		if (fileName.isEmpty()) {
			return null;
		}
		return ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/").path(fileName).toUriString();
	}
}
