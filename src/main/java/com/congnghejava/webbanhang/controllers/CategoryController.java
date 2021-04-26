package com.congnghejava.webbanhang.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

import com.congnghejava.webbanhang.models.Category;
import com.congnghejava.webbanhang.payload.request.CategoryRequest;
import com.congnghejava.webbanhang.payload.response.MessageResponse;
import com.congnghejava.webbanhang.services.CategoryServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	CategoryServiceImpl categoryService;

	@GetMapping
	public ResponseEntity<Iterable<Category>> getAllCategory() {
		return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Category> getCategory(@PathVariable Long id) {
		Optional<Category> categoryOptional = categoryService.findById(id);

		return categoryOptional.map(tempCategory -> new ResponseEntity<>(tempCategory, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> createNewCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
		if (categoryService.existsCategoryName(categoryRequest.getName())) {
			return ResponseEntity.badRequest().body(new MessageResponse("That category already exists"));
		}

		Category category = new Category(categoryRequest.getName());
		categoryService.save(category);

		return new ResponseEntity<>(new MessageResponse("Add category '" + category.getName() + "' successfully!"),
				HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateCategory(@PathVariable Long id,
			@Valid @RequestBody CategoryRequest categoryRequest) {
		if (categoryService.existsCategoryName(categoryRequest.getName())) {
			return ResponseEntity.badRequest().body(new MessageResponse("That category already exists"));
		}

		Optional<Category> categoryOptional = categoryService.findById(id);

		return categoryOptional.map(oldCategory -> {
			Category category = new Category(categoryRequest.getName());
			String oldCategoryName = oldCategory.getName();

			category.setId(oldCategory.getId());
			categoryService.save(category);

			return ResponseEntity.ok().body(
					new MessageResponse("Updated category '" + oldCategoryName + "' to '" + category.getName() + "'"));
		}).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Category> deleteCategory(@PathVariable Long id) {
		Optional<Category> categoryOptional = categoryService.findById(id);

		return categoryOptional.map(tempCategory -> {
			categoryService.remove(id);

			return new ResponseEntity<>(tempCategory, HttpStatus.OK);
		}).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}
