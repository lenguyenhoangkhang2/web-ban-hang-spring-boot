package com.congnghejava.webbanhang.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.ProductPage;
import com.congnghejava.webbanhang.models.ProductSearchCriteria;
import com.congnghejava.webbanhang.payload.response.ProductResponse;

@Repository
public class ProductCriteriaRepository {
	private final EntityManager entityManager;
	private final CriteriaBuilder criteriaBuilder;

	public ProductCriteriaRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.criteriaBuilder = entityManager.getCriteriaBuilder();
	}

	public Page<ProductResponse> findAllWithFilter(ProductPage productPage,
			ProductSearchCriteria productSearchCriteria) {
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

		Root<Product> productRoot = criteriaQuery.from(Product.class);
		Predicate predicate = getPredicate(productSearchCriteria, productRoot);

		criteriaQuery.where(predicate);
		setOrder(productPage, criteriaQuery, productRoot);

		TypedQuery<Product> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setFirstResult(productPage.getPage() * productPage.getSize());
		typedQuery.setMaxResults(productPage.getSize());

		Pageable pageable = getPageable(productPage);
		long productCount = getProductCount(predicate);

		List<ProductResponse> productResponses = typedQuery.getResultStream()
				.map(product -> new ProductResponse(product)).collect(Collectors.toList());

		return new PageImpl<ProductResponse>(productResponses, pageable, productCount);
	}

	private long getProductCount(Predicate predicate) {
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<Product> countRoot = countQuery.from(Product.class);
		countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
		return entityManager.createQuery(countQuery).getSingleResult();
	}

	private Pageable getPageable(ProductPage productPage) {
		Sort sort = Sort.by(productPage.getSortDirection(), productPage.getSortBy());
		return PageRequest.of(productPage.getPage(), productPage.getSize(), sort);

	}

	private void setOrder(ProductPage productPage, CriteriaQuery<Product> criteriaQuery, Root<Product> productRoot) {
		if (productPage.getSortDirection().equals(Sort.Direction.ASC)) {
			criteriaQuery.orderBy(criteriaBuilder.asc(productRoot.get(productPage.getSortBy())));
		} else {
			criteriaQuery.orderBy(criteriaBuilder.desc(productRoot.get(productPage.getSortBy())));
		}
	}

	private Predicate getPredicate(ProductSearchCriteria productSearchCriteria, Root<Product> productRoot) {
		List<Predicate> predicates = new ArrayList<>();

		if (Objects.nonNull(productSearchCriteria.getName())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(productRoot.get("name")),
					"%" + productSearchCriteria.getName().toUpperCase() + "%"));
		}
		if (Objects.nonNull(productSearchCriteria.getBrand())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(productRoot.get("brand").get("name")),
					"%" + productSearchCriteria.getBrand().toUpperCase() + "%"));
		}
		if (Objects.nonNull(productSearchCriteria.getCatorory())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(productRoot.get("category").get("name")),
					"%" + productSearchCriteria.getCatorory().toUpperCase() + "%"));
		}

		if (Objects.nonNull(productSearchCriteria.getMaxPrice())) {
			predicates.add(
					criteriaBuilder.lessThanOrEqualTo(productRoot.get("price"), productSearchCriteria.getMaxPrice()));
		}
		if (Objects.nonNull(productSearchCriteria.getMinPrice())) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(productRoot.get("price"),
					productSearchCriteria.getMinPrice()));
		}
		predicates.add(criteriaBuilder.greaterThan(productRoot.get("quantity"), 0));
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}

	public Long calPrice(Long price, Long discount) {
		return Math.round(Double.valueOf(price * (100 - discount) / 100) / 10000) * 10000;
	}
}
