package com.congnghejava.webbanhang.payload.response;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.congnghejava.webbanhang.models.Order;

public class OrderResponse {
	private String id;
	private String createdDate;
	private String updatedDate;
	private String status;
	private HashMap<String, Object> userInfo = new HashMap<>();
	private long total;

	private List<OrderItemResponse> items;

	public OrderResponse(Order order) {
		this.id = order.getId();
		this.status = order.getStatus().toString();
		this.createdDate = new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(order.getCreatedDate());
		this.updatedDate = new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(order.getLastModifiedDate());
		this.items = order.getOrderItems().stream().map(orderItem -> new OrderItemResponse(orderItem))
				.collect(Collectors.toList());
		this.total = order.getTotal();

		this.userInfo.put("name", order.getUser().getName());
		this.userInfo.put("email", order.getUser().getUserCredential().getEmail());
		this.userInfo.put("country", order.getUser().getUserInfo().getCountry());
		this.userInfo.put("province", order.getUser().getUserInfo().getProvince());
		this.userInfo.put("district", order.getUser().getUserInfo().getDistrict());
		this.userInfo.put("detail", order.getUser().getUserInfo().getDetail());
		this.userInfo.put("phone", order.getUser().getUserInfo().getPhone());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<OrderItemResponse> getItems() {
		return items;
	}

	public void setItems(List<OrderItemResponse> items) {
		this.items = items;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public HashMap<String, Object> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(HashMap<String, Object> userInfo) {
		this.userInfo = userInfo;
	}

}
