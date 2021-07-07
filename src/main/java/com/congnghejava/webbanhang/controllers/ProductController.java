package com.congnghejava.webbanhang.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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

import com.congnghejava.webbanhang.exception.BadRequestException;
import com.congnghejava.webbanhang.models.Brand;
import com.congnghejava.webbanhang.models.Category;
import com.congnghejava.webbanhang.models.EProductBrand;
import com.congnghejava.webbanhang.models.EProductCategory;
import com.congnghejava.webbanhang.models.EProductImageTypeDisplay;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.ProductDetails;
import com.congnghejava.webbanhang.models.ProductPage;
import com.congnghejava.webbanhang.models.Review;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.payload.request.ProductRequest;
import com.congnghejava.webbanhang.payload.request.ReviewRequest;
import com.congnghejava.webbanhang.payload.response.MessageResponse;
import com.congnghejava.webbanhang.payload.response.ProductResponse;
import com.congnghejava.webbanhang.payload.response.ReviewResponse;
import com.congnghejava.webbanhang.repository.criteria.ProductSearchCriteria;
import com.congnghejava.webbanhang.security.CurrentUser;
import com.congnghejava.webbanhang.security.UserPrincipal;
import com.congnghejava.webbanhang.services.BrandServiceImpl;
import com.congnghejava.webbanhang.services.CartService;
import com.congnghejava.webbanhang.services.CategoryServiceImpl;
import com.congnghejava.webbanhang.services.FileStorageServiceImpl;
import com.congnghejava.webbanhang.services.ProductDetailsServiceImpl;
import com.congnghejava.webbanhang.services.ProductImageServiceImpl;
import com.congnghejava.webbanhang.services.ProductService;
import com.congnghejava.webbanhang.services.ReviewServiceImpl;
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
	ProductDetailsServiceImpl productDetailsService;

	@Autowired
	ReviewServiceImpl reviewService;

	@Autowired
	CartService cartService;

	// @formatter:off
	@GetMapping
	public ResponseEntity<?> getProducts(@RequestParam(value = "page") Optional<Integer> page,
										   @RequestParam(value = "size") Optional<Integer> size,
										   @RequestParam(value = "sortDirection") Optional<String> sortDirection,
										   @RequestParam(value = "sortBy") Optional<String> sortBy,
										   @RequestParam(value = "name") Optional<String> name,
										   @RequestParam(value = "brand") Optional<String> brand,
										   @RequestParam(value = "category") Optional<String> category,
										   @RequestParam(value = "minPrice") Optional<Integer> minPrice,
										   @RequestParam(value = "maxPrice") Optional<Integer> maxPrice) {
	// @formatter:on
		ProductPage productPage = new ProductPage();
		ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria();

		if (page.isPresent()) {
			productPage.setPage(page.get());
		}
		if (size.isPresent()) {
			productPage.setSize(size.get());
		}
		if (sortDirection.isPresent()) {
			productPage.setSortDirection(sortDirection.get());
		}
		if (sortBy.isPresent()) {
			productPage.setSortBy(sortBy.get());
		}

		if (name.isPresent()) {
			productSearchCriteria.setName(name.get());
		}
		if (brand.isPresent()) {
			productSearchCriteria.setBrand(brand.get());
		}
		if (category.isPresent()) {
			productSearchCriteria.setCatorory(category.get());
		}
		if (minPrice.isPresent()) {
			productSearchCriteria.setMinPrice(minPrice.get());
		}
		if (maxPrice.isPresent()) {
			productSearchCriteria.setMaxPrice(maxPrice.get());
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
	public ResponseEntity<?> addProduct(@Valid @RequestPart("product") ProductRequest productRequest,
										@RequestPart("official") MultipartFile official, 
										@RequestPart("slider") MultipartFile[] slider, 
										@CurrentUser UserPrincipal userPrincipal) {
	// @formatter:on
		User user = userService.getCurrentUser(userPrincipal);
		Category category = getCategoryFromProductRequest(productRequest);
		Brand brand = getBrandFromProductRequest(productRequest);
		ProductDetails details = getProductDetailsFromRequest(productRequest);
		// @formatter:off
		Product product = new Product(productRequest.getName(), 
									  productRequest.getDescription(),
									  category, 
									  brand,
									  productRequest.getPrice(), 
									  productRequest.getQuantity(),
									  productRequest.getDiscount(),
									  details);
		// @formatter:on
		product.setUser(user);
		productService.save(product);

		try {
			fileStorageService.saveImageForProduct(official, product, EProductImageTypeDisplay.Official);

			Arrays.asList(slider).stream().forEach(file -> {
				fileStorageService.saveImageForProduct(file, product, EProductImageTypeDisplay.Slider);
			});
			return ResponseEntity.status(HttpStatus.OK)
					.body(new MessageResponse("Đã thêm vào sản phẩm " + product.getName()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new MessageResponse(e.getLocalizedMessage()));
		}
	}

	// @formatter:off
	@PutMapping(path =  "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateProduct(@RequestPart("product") ProductRequest productRequest, 
										   @RequestPart(value = "official", required = false) MultipartFile official,
										   @RequestPart(value = "banner", required = false) MultipartFile banner,
										   @RequestPart(value = "slider", required = false) MultipartFile[] slider, 
										   @PathVariable Long id,
										   @CurrentUser UserPrincipal userPrincipal) {
	// @formatter:on
		Product oldProduct = productService.findById(id);

		User user = userService.getCurrentUser(userPrincipal);

		Category category = getCategoryFromProductRequest(productRequest);

		Brand brand = getBrandFromProductRequest(productRequest);

		ProductDetails details = getProductDetailsFromRequest(productRequest);
		details.setId(oldProduct.getDetails().getId());

		// @formatter:off
		Product product = new Product(productRequest.getName(), 
									  productRequest.getDescription(),
									  category, 
									  brand,
									  productRequest.getPrice(), 
									  productRequest.getQuantity(),
									  productRequest.getDiscount(),
									  details);
		// @formatter:on
		product.setUser(user);
		product.setId(oldProduct.getId());
		productService.save(product);

		product.setUser(oldProduct.getUser());

		productService.save(product);
		// @formatter:on

		if (Objects.nonNull(official)) {
			productImageService.deleteByProductAndType(product, EProductImageTypeDisplay.Official);
			fileStorageService.saveImageForProduct(official, product, EProductImageTypeDisplay.Official);
		}

		if (Objects.nonNull(slider)) {
			productImageService.deleteByProductAndType(product, EProductImageTypeDisplay.Slider);
			Arrays.asList(slider).stream().forEach(image -> {
				fileStorageService.saveImageForProduct(image, product, EProductImageTypeDisplay.Slider);
			});
		}

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Sửa đổi thông tin sản phẩm thành công"));

	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		try {
			Product product = productService.findById(id);
			productImageService.deleteByProduct(product);
			cartService.findByProduct(product).forEach(cart -> {
				cartService.remove(cart.getId());
			});
			productService.remove(id);
			return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse(e.getLocalizedMessage()));
		}

	}

	@GetMapping("/category")
	public ResponseEntity<?> getTypeProduct() {
		return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList(EProductCategory.values()));
	}

	@GetMapping("/brand")
	public ResponseEntity<?> getBrandProduct() {
		return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList(EProductBrand.values()));
	}

	// @formatter:off
	@GetMapping("/{productId}/reviews")
	public ResponseEntity<?> getProductReview(@PathVariable Long productId,
							 @PageableDefault(page = 0, size = 5)
							 @SortDefault.SortDefaults(@SortDefault(sort = "Time", direction = Direction.DESC)) 
							 Pageable pageable) {
	
		Product product = productService.findById(productId);
		List<ReviewResponse> reviewResponse = reviewService.findByProduct(product).stream()
															.map(review -> new ReviewResponse(review))
															.collect(Collectors.toList());
		// @formatter:on
		return ResponseEntity.status(HttpStatus.OK)
				.body(new PageImpl<>(reviewResponse, pageable, reviewResponse.size()));

	}

	@GetMapping("/{productId}/reviewed")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> productIsReviewedByUser(@PathVariable Long productId,
			@CurrentUser UserPrincipal userPrincipal) {
		Product product = productService.findById(productId);
		User user = userService.getCurrentUser(userPrincipal);
		boolean isReviewd = reviewService.existsByUserAndProduct(user, product);

		return ResponseEntity.ok(isReviewd);
	}

	// @formatter:off
	@PostMapping("/{productId}/reviews")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> addProductReview(@PathVariable Long productId,
											  @RequestBody ReviewRequest reviewRequest,
											  @CurrentUser UserPrincipal userPiPrincipal) {
	// @formatter:on
		User user = userService.getCurrentUser(userPiPrincipal);
		Product product = productService.findById(productId);

		if (reviewService.existsByUserAndProduct(user, product)) {
			throw new BadRequestException("Bạn đã bình luận sản phẩm này trước đó");
		}

		Review review = new Review(reviewRequest.getRating(), reviewRequest.getComment(), product, user);
		reviewService.save(review);

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Cám ơn bạn đã nhận xét sản phẩm"));
	}

	private Brand getBrandFromProductRequest(ProductRequest productRequest) {

		String brandRequest = productRequest.getBrandName();
		if (!brandService.existsBrandName(brandRequest)) {
			return brandService.save(new Brand(EProductBrand.valueOf(brandRequest)));
		} else {
			return brandService.findFirstByBrandName(brandRequest);
		}
	}

	private Category getCategoryFromProductRequest(ProductRequest productRequest) {
		String categoryRequest = productRequest.getCategoryName();

		if (!categoryService.existsCategoryName(categoryRequest)) {
			return categoryService.save(new Category(EProductCategory.valueOf(categoryRequest)));
		} else {
			return categoryService.findFirstByCategoryName(categoryRequest);
		}
	}

	private ProductDetails getProductDetailsFromRequest(ProductRequest productRequest) {
		EProductCategory category = EProductCategory.valueOf(productRequest.getCategoryName());
		ProductDetails details = new ProductDetails();
		switch (category) {
		case Laptop:
			details.setCpu(productRequest.getCpu());
			details.setRam(productRequest.getRam());
			details.setHardDrive(productRequest.getHardDrive());
			details.setDisplay(productRequest.getDisplay());
			details.setSize(productRequest.getSize());
			details.setOperatingSystem(productRequest.getOperatingSystem());
			details.setDesign(productRequest.getDesign());
			details.setGraphicsCard(productRequest.getGraphicsCard());
			break;
		case SmartPhone:
			details.setDisplay(productRequest.getDisplay());
			details.setOperatingSystem(productRequest.getOperatingSystem());
			details.setFontCamera(productRequest.getFontCamera());
			details.setRearCamera(productRequest.getRearCamera());
			details.setChipName(productRequest.getChipName());
			details.setRam(productRequest.getRam());
			details.setInternalMemory(productRequest.getInternalMemory());
			details.setSim(productRequest.getSim());
			details.setBatteryCapacity(productRequest.getBatteryCapacity());
			break;
		default:
			break;
		}
		return details;
	}

}
