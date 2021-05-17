package com.congnghejava.webbanhang.models;

import org.springframework.data.domain.Sort;

public class ProductPage {
	private int pageNumber = 0;
	private int pageSize = 10;
	private Sort.Direction sorDirection = Sort.Direction.ASC;
	private String sortBy = "createdDate";

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Sort.Direction getSorDirection() {
		return sorDirection;
	}

	public void setSorDirection(Sort.Direction sorDirection) {
		this.sorDirection = sorDirection;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

}
