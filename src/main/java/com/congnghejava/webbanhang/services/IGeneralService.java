package com.congnghejava.webbanhang.services;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<T> {
	List<T> findAll();

	Optional<T> findById(Long theId);

	T save(T t);

	void remove(Long theId);

}
