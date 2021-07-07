package com.congnghejava.webbanhang.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
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
import com.congnghejava.webbanhang.payload.response.ProductResponse;
import com.congnghejava.webbanhang.repository.criteria.ProductSearchCriteria;

@Repository
public class ProductCriteriaRepository {
	private final EntityManager entityManager;
	private final CriteriaBuilder cb;

	public ProductCriteriaRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.cb = entityManager.getCriteriaBuilder();
	}

	public Page<ProductResponse> findAllWithFilter(ProductPage productPage,
			ProductSearchCriteria productSearchCriteria) {
		CriteriaQuery<Product> criteriaQuery = cb.createQuery(Product.class);

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
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<Product> countRoot = countQuery.from(Product.class);
		countQuery.select(cb.count(countRoot)).where(predicate);
		return entityManager.createQuery(countQuery).getSingleResult();
	}

	private Pageable getPageable(ProductPage productPage) {
		return PageRequest.of(productPage.getPage(), productPage.getSize());
	}

	private void setOrder(ProductPage productPage, CriteriaQuery<Product> criteriaQuery, Root<Product> productRoot) {

		if (productPage.getSortDirection().equals(Sort.Direction.ASC)) {
			criteriaQuery.orderBy(cb.asc(getOrderExpression(productPage.getSortBy(), productRoot)));
		} else {
			criteriaQuery.orderBy(cb.desc(getOrderExpression(productPage.getSortBy(), productRoot)));
		}
	}

	private Expression<?> getOrderExpression(String sortBy, Root<Product> root) {
		switch (sortBy) {
		case "createdDate":
			return root.get("createdDate");
		case "price":
			return cb.function("ROUND", Integer.class,
					cb.prod(root.get("price"), cb.quot(cb.diff(100, root.get("discount")), 100)));
		default:
			return cb.conjunction();
		}
	}

	private Predicate getPredicate(ProductSearchCriteria productSearchCriteria, Root<Product> productRoot) {
		List<Predicate> predicates = new ArrayList<>();

		if (Objects.nonNull(productSearchCriteria.getName())) {
			predicates.add(cb.like(cb.upper(productRoot.get("name")),
					"%" + productSearchCriteria.getName().toUpperCase() + "%"));
		}

		if (Objects.nonNull(productSearchCriteria.getBrand())) {
			predicates.add(cb.like(cb.upper(productRoot.get("brand").get("name")),
					"%" + productSearchCriteria.getBrand().toUpperCase() + "%"));
		}

		if (Objects.nonNull(productSearchCriteria.getCatorory())) {
			predicates.add(cb.like(cb.upper(productRoot.get("category").get("name")),
					"%" + productSearchCriteria.getCatorory().toUpperCase() + "%"));
		}

		if (Objects.nonNull(productSearchCriteria.getMaxPrice())) {
			predicates
					.add(cb.lessThanOrEqualTo(
							cb.function("ROUND", Integer.class,
									cb.prod(productRoot.get("price"),
											cb.quot(cb.diff(100, productRoot.get("discount")), 100))),
							productSearchCriteria.getMaxPrice()));
		}

		if (Objects.nonNull(productSearchCriteria.getMinPrice())) {
			predicates
					.add(cb.greaterThanOrEqualTo(
							cb.function("ROUND", Integer.class,
									cb.prod(productRoot.get("price"),
											cb.quot(cb.diff(100, productRoot.get("discount")), 100)),
									cb.literal("-4")),
							productSearchCriteria.getMinPrice()));
		}
		predicates.add(cb.greaterThan(productRoot.get("quantity"), 0));
		return cb.and(predicates.toArray(new Predicate[0]));
	}

}
