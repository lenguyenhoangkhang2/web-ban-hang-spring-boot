package com.congnghejava.webbanhang.repository.criteria;

public class OrderSearchCriteria {
	private String id;
	private String username;
	private String status;

	public OrderSearchCriteria() {
	}

	public OrderSearchCriteria(String id, String username, String status) {
		this.id = id;
		this.username = username;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getStatus() {
		return status;
	}

}
