package com.congnghejava.webbanhang.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.congnghejava.webbanhang.exception.BadRequestException;
import com.congnghejava.webbanhang.models.EHeaderImageType;
import com.congnghejava.webbanhang.models.HeaderImage;
import com.congnghejava.webbanhang.payload.request.HeaderImageRequest;
import com.congnghejava.webbanhang.payload.response.HeaderImageResponse;
import com.congnghejava.webbanhang.payload.response.MessageResponse;
import com.congnghejava.webbanhang.services.FileStorageServiceImpl;
import com.congnghejava.webbanhang.services.HeaderImageService;

@RestController
@RequestMapping("/header-images")
public class HeaderImageController {

	@Autowired
	HeaderImageService headerImageService;

	@Autowired
	FileStorageServiceImpl fileStorageService;

	@GetMapping
	public ResponseEntity<List<HeaderImageResponse>> getHeaderImages(
			@RequestParam(name = "enable", required = false) Optional<Boolean> enable,
			@RequestParam("type") Optional<EHeaderImageType> type) {
		List<HeaderImage> listHeaderImage = new ArrayList<>();
		if (enable.isPresent() && type.isPresent()) {
			listHeaderImage = headerImageService.findByTypeAndEnable(type.get(), enable.get());
		} else {
			listHeaderImage = headerImageService.findAll();
		}

		List<HeaderImageResponse> response = listHeaderImage.stream().map(i -> new HeaderImageResponse(i))
				.collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}

	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> addHeaderImage(@RequestPart("meta-data") HeaderImageRequest headerImageRequest,
			@RequestPart("image") MultipartFile image) {
		if (headerImageRequest.getType().equals("Banner")) {
			List<HeaderImage> listBannnerActive = headerImageService.findByTypeAndEnable(EHeaderImageType.Banner, true);

			if (listBannnerActive.size() >= 4) {
				throw new BadRequestException("Số lượng banner hiển thị đã đủ");
			}
		}

		String fileName = fileStorageService.save(image);

		// @formatter:off 
		HeaderImage headerImage = new HeaderImage(headerImageRequest.getTitle(), 
												  fileName,
												  headerImageRequest.getLinkTo(), 
												  EHeaderImageType.valueOf(headerImageRequest.getType()),
												  headerImageRequest.getEnable());
		// @formatter:on
		headerImageService.save(headerImage);
		return ResponseEntity.ok(new MessageResponse("Đã thêm ảnh thành công"));
	}

	@PutMapping(path = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<MessageResponse> updateHeaderImage(
			@RequestPart("meta-data") HeaderImageRequest headerImageRequest,
			@RequestPart("image") Optional<MultipartFile> image, @PathVariable("id") Integer id) {
		if (headerImageRequest.getType().equals("Banner")) {
			List<HeaderImage> listBannnerActive = headerImageService.findByTypeAndEnable(EHeaderImageType.Banner, true);

			if (listBannnerActive.size() >= 4) {
				throw new BadRequestException("Số lượng banner hiển thị đã đủ");
			}
		}

		HeaderImage oldHeaderImage = headerImageService.findById(id);
		// @formatter:off 
		HeaderImage headerImage = new HeaderImage(headerImageRequest.getTitle(), 
												  oldHeaderImage.getFileUploadName(),
												  headerImageRequest.getLinkTo(), 
												  EHeaderImageType.valueOf(headerImageRequest.getType()),
												  headerImageRequest.getEnable());
		// @formatter:on
		if (image.isPresent()) {
			String fileUploadName = fileStorageService.save(image.get());
			fileStorageService.delete(oldHeaderImage.getFileUploadName());
			headerImage.setFileUploadName(fileUploadName);
		}

		headerImage.setId(id);
		headerImageService.save(headerImage);
		return ResponseEntity.ok(new MessageResponse("Cập nhật thông tin hình ảnh thành công"));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<MessageResponse> deleteHeaderImage(@PathVariable("id") Integer id) {
		HeaderImage image = headerImageService.findById(id);
		fileStorageService.delete(image.getFileUploadName());
		headerImageService.delete(image);
		return ResponseEntity.ok(new MessageResponse("Đã xóa hình ảnh"));
	}
}
