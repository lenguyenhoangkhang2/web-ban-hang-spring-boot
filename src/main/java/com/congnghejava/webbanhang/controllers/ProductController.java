package com.congnghejava.webbanhang.controllers;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.congnghejava.webbanhang.models.Brand;
import com.congnghejava.webbanhang.models.Category;
import com.congnghejava.webbanhang.models.EProductImageTypeDisplay;
import com.congnghejava.webbanhang.models.EProductCategory;
import com.congnghejava.webbanhang.models.LaptopDetails;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.ProductPage;
import com.congnghejava.webbanhang.models.ProductSearchCriteria;
import com.congnghejava.webbanhang.models.SmartPhoneDetails;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.payload.request.ProductRequest;
import com.congnghejava.webbanhang.payload.response.MessageResponse;
import com.congnghejava.webbanhang.payload.response.ProductResponse;
import com.congnghejava.webbanhang.security.CurrentUser;
import com.congnghejava.webbanhang.security.UserPrincipal;
import com.congnghejava.webbanhang.services.BrandServiceImpl;
import com.congnghejava.webbanhang.services.CategoryServiceImpl;
import com.congnghejava.webbanhang.services.FileStorageServiceImpl;
import com.congnghejava.webbanhang.services.LaptopDetailsServiceImpl;
import com.congnghejava.webbanhang.services.ProductImageServiceImpl;
import com.congnghejava.webbanhang.services.ProductService;
import com.congnghejava.webbanhang.services.SmartPhoneDetailsServiceImpl;
import com.congnghejava.webbanhang.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController()
@RequestMapping("/products")
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	UserService userService;

	@Autowired
	FileStorageServiceImpl fileStorageService;

	@Autowired
	ProductImageServiceImpl productImageService;

	@Autowired
	CategoryServiceImpl categoryService;

	@Autowired
	BrandServiceImpl brandService;

	@Autowired
	LaptopDetailsServiceImpl laptopDetailsService;

	@Autowired
	SmartPhoneDetailsServiceImpl smartPhoneDetailsService;

	@GetMapping
	public ResponseEntity<?> getAllProduct(ProductPage productPage, @RequestParam(value = "name") Optional<String> name,
			@RequestParam(value = "brand") Optional<String> brand,
			@RequestParam(value = "category") Optional<String> category) {
		ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria();

		if (name.isPresent()) {
			productSearchCriteria.setName(name.get());
		}
		if (brand.isPresent()) {
			productSearchCriteria.setBrand(brand.get());
		}
		if (category.isPresent()) {
			productSearchCriteria.setCatorory(category.get());
		}

		return new ResponseEntity<>(productService.findAllWithFilter(productPage, productSearchCriteria),
				HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ProductResponse getProduct(@PathVariable Long id) {
		Product product = productService.findById(id);
		return new ProductResponse(product);
	}

	// @formatter:off
	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> addProduct(@RequestPart("product") @Valid ProductRequest productRequest,
										@RequestPart("official") MultipartFile official, 
										@RequestPart("banner") MultipartFile banner,
										@RequestPart("slider") MultipartFile[] slider, 
										@CurrentUser UserPrincipal userPrincipal) {
	// @formatter:on
		User user = userService.getCurrentUser(userPrincipal);

		Brand brandProduct = brandService.findById(productRequest.getBrandId()).get();

		Category categoryProduct = categoryService.findById(productRequest.getCategoryId()).get();

		EProductCategory productType = EProductCategory.valueOf(productRequest.getType());

		try {
			// @formatter:off
			Product product = new Product(productType, 
										  productRequest.getName(), 
										  productRequest.getDescription(),
										  categoryProduct, 
										  brandProduct,
										  productRequest.getPrice(), 
										  productRequest.getQuantity(),
										  productRequest.getDiscount());

			product.setUser(user);
			productService.save(product);
			// @formatter:on

			fileStorageService.saveImageForProduct(official, product, EProductImageTypeDisplay.Official);

			fileStorageService.saveImageForProduct(banner, product, EProductImageTypeDisplay.Banner);

			Arrays.asList(slider).stream().forEach(file -> {
				fileStorageService.saveImageForProduct(file, product, EProductImageTypeDisplay.Slider);
			});

			// @formatter:off
			switch (productType) {
			case Laptop:
				LaptopDetails laptopDetails = new LaptopDetails(product, 
																productRequest.getCpu(),
																productRequest.getRam(), 
																productRequest.getHardDrive(), 
																productRequest.getDisplay(),
																productRequest.getSize(), 
																productRequest.getOperatingSystem(), 
																productRequest.getDesign());
				laptopDetailsService.save(laptopDetails);
				break;
			case SmartPhone:
				SmartPhoneDetails smartPhoneDetails = new SmartPhoneDetails(product, 
																			productRequest.getDisplay(),
																			productRequest.getOperatingSystem(), 
																			productRequest.getFontCamera(),
																			productRequest.getRearCamera(), 
																			productRequest.getChipName(),
																			productRequest.getInternalMemory(), 
																			productRequest.getExternalMemory(), 
																			productRequest.getSim(),
																			productRequest.getBatteryCapacity());
				smartPhoneDetailsService.save(smartPhoneDetails);
				break;
			default:
				break;
			}
			// @formatter:on

			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Create product successfully"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new MessageResponse(e.getLocalizedMessage()));
		}
	}

	// @formatter:off
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateProduct(@RequestPart(value = "official", required = false) MultipartFile official,
										@RequestPart(value = "banner", required = false) MultipartFile banner,
										@RequestPart(value = "slider", required = false) MultipartFile[] slider, 
										@PathVariable Long id,
										@RequestBody ProductRequest productRequest, UserPrincipal userPrincipal) {
	// @formatter:on
		Product oldProduct = productService.findById(id);

		Brand brandProduct = brandService.findById(productRequest.getBrandId()).get();
		Category categoryProduct = categoryService.findById(productRequest.getCategoryId()).get();

		EProductCategory productType = EProductCategory.valueOf(productRequest.getType());

		try {
			// @formatter:off
			Product product = new Product(productType, 
					  productRequest.getName(), 
					  productRequest.getDescription(),
					  categoryProduct, brandProduct, 
					  productRequest.getPrice(), 
					  productRequest.getQuantity(),
					  productRequest.getDiscount());

			product.setId(oldProduct.getId());
			product.setUser(oldProduct.getUser());
			
			productService.save(product);
			// @formatter:on

			if (Objects.nonNull(official)) {
				productImageService.deleteByProductAndType(product, EProductImageTypeDisplay.Official);
				fileStorageService.saveImageForProduct(official, product, EProductImageTypeDisplay.Official);
			}

			if (Objects.nonNull(banner)) {
				productImageService.deleteByProductAndType(product, EProductImageTypeDisplay.Banner);
				fileStorageService.saveImageForProduct(banner, product, EProductImageTypeDisplay.Banner);
			}

			if (Objects.nonNull(slider)) {
				productImageService.deleteByProductAndType(product, EProductImageTypeDisplay.Slider);
				Arrays.asList(slider).stream().forEach(image -> {
					fileStorageService.saveImageForProduct(image, product, EProductImageTypeDisplay.Slider);
				});
			}

			// @formatter:off
			switch (productType) {
			case Laptop:
				Long oldLaptopDetailsId = laptopDetailsService.findByProduct(oldProduct).getId();
				LaptopDetails laptopDetails = new LaptopDetails(product, 
																productRequest.getCpu(),
																productRequest.getRam(), 
																productRequest.getHardDrive(), 
																productRequest.getDisplay(),
																productRequest.getSize(), 
																productRequest.getOperatingSystem(), 
																productRequest.getDesign());
				laptopDetails.setId(oldLaptopDetailsId);
				laptopDetailsService.save(laptopDetails);
				break;
			case SmartPhone:
				Long oldSmartPhoneDetailsId = smartPhoneDetailsService.findByProduct(oldProduct).getId();
				SmartPhoneDetails smartPhoneDetails = new SmartPhoneDetails(product, 
																			productRequest.getDisplay(),
																			productRequest.getOperatingSystem(), 
																			productRequest.getFontCamera(),
																			productRequest.getRearCamera(), 
																			productRequest.getChipName(),
																			productRequest.getInternalMemory(), 
																			productRequest.getExternalMemory(), 
																			productRequest.getSim(),
																			productRequest.getBatteryCapacity());
				smartPhoneDetails.setId(oldSmartPhoneDetailsId);
				smartPhoneDetailsService.save(smartPhoneDetails);
				break;
			default:
				break;
			}
			// @formatter:on

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new MessageResponse(e.getLocalizedMessage()));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Updated product successfully"));

	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		Product product = productService.findById(id);
		productImageService.deleteByProduct(product);
		productService.remove(id);
		return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
	}

	@GetMapping("/type")
	public ResponseEntity<?> getTypeProduct() {
		return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList(EProductCategory.values()));
	}
}
