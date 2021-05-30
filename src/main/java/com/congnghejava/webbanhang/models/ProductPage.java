package com.congnghejava.webbanhang.models;

import org.springframework.data.domain.Sort;

public class ProductPage {
	private int page = 0;
	private int size = 10;
	private Sort.Direction sortDirection;
	private String sortBy;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Sort.Direction getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		if (sortDirection.equals("asc")) {
			this.sortDirection = Sort.Direction.ASC;
		}
		if (sortDirection.equals("desc")) {
			this.sortDirection = Sort.Direction.DESC;
		}
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

}
