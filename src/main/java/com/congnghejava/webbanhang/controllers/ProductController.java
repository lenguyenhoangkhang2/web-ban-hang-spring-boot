package com.congnghejava.webbanhang.controllers;

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
import com.congnghejava.webbanhang.models.FileDB;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.ProductPage;
import com.congnghejava.webbanhang.models.ProductSearchCriteria;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.payload.request.AddProductRequest;
import com.congnghejava.webbanhang.payload.response.MessageResponse;
import com.congnghejava.webbanhang.payload.response.ProductResponse;
import com.congnghejava.webbanhang.security.CurrentUser;
import com.congnghejava.webbanhang.security.UserPrincipal;
import com.congnghejava.webbanhang.services.BrandServiceImpl;
import com.congnghejava.webbanhang.services.CategoryServiceImpl;
import com.congnghejava.webbanhang.services.FileStorageService;
import com.congnghejava.webbanhang.services.ProductService;
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
	FileStorageService fileStorageService;

	@Autowired
	CategoryServiceImpl categoryService;

	@Autowired
	private BrandServiceImpl brandService;

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

	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> addProduct(@RequestPart("product") @Valid AddProductRequest productRequest,
			@RequestPart("product_image") MultipartFile file, @CurrentUser UserPrincipal userPrincipal) {
		System.out.println(userPrincipal);
		User user = userService.getCurrentUser(userPrincipal);

		Brand brandProduct = brandService.findById(productRequest.getBrandId()).get();
		Category categoryProduct = categoryService.findById(productRequest.getCategoryId()).get();

		Product product = new Product(productRequest.getName(), productRequest.getDescription(), categoryProduct,
				brandProduct, productRequest.getPrice(), productRequest.getQuantity(), productRequest.getDiscount());

		FileDB image = new FileDB();
		try {
			image = fileStorageService.store(file, userService.getCurrentUser(userPrincipal));
		} catch (Exception e) {
			String message = "Could not upload file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}

		product.setUser(user);
		product.setImage(image);

		productService.save(product);

		return ResponseEntity.status(HttpStatus.OK).body(new ProductResponse(product));

	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateProduct(@RequestParam(value = "product_image", required = false) MultipartFile file,
			@PathVariable Long id, @RequestBody AddProductRequest productRequest, UserPrincipal userPrincipal) {
		Product oldProduct = productService.findById(id);

		Brand brandProduct = brandService.findById(productRequest.getBrandId()).get();
		Category categoryProduct = categoryService.findById(productRequest.getCategoryId()).get();

		Product product = new Product(productRequest.getName(), productRequest.getDescription(), categoryProduct,
				brandProduct, productRequest.getPrice(), productRequest.getQuantity(), productRequest.getDiscount());

		FileDB image = new FileDB();

		if (file.isEmpty()) {
			product.setImage(oldProduct.getImage());
		} else {
			try {
				image = fileStorageService.store(file, userService.getCurrentUser(userPrincipal));
			} catch (Exception e) {
				String message = "Could not upload file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
			}
			product.setImage(image);
		}

		product.setId(oldProduct.getId());
		product.setUser(oldProduct.getUser());

		productService.save(product);
		return ResponseEntity.status(HttpStatus.OK).body(new ProductResponse(product));

	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		if (productService.findById(id) != null) {
			productService.remove(id);
		}
		return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
	}
}
