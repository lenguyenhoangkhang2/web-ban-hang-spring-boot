package com.congnghejava.webbanhang.services;

import com.congnghejava.webbanhang.models.Product;

public interface ProductDetailsGeneralService<T> {
	public void deleteByProduct(Product product);

	public T save(T t);

	public T findByProduct(Product product);
}
