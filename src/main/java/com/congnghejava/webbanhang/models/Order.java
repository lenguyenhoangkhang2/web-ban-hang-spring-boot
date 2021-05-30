package com.congnghejava.webbanhang.models;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderItem> orderItems = new HashSet<>();

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private EOrderStatus status;

	private Boolean settled = false;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method")
	private EPaymentMethod paymentMethod;

	private String stripeChagreId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	public Order() {
	}

	public Order(User user, List<Cart> listCart) {
		this.user = user;
		listCart.forEach(cart -> {
			orderItems.add(new OrderItem(this, cart));
		});
		this.status = EOrderStatus.Open;
		this.createdDate = new Date();
		this.updatedDate = new Date();
		this.paymentMethod = EPaymentMethod.Cod;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public EOrderStatus getStatus() {
		return status;
	}

	public void setStatus(EOrderStatus status) {
		this.status = status;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public boolean isSettled() {
		return settled;
	}

	public void setSettled(boolean settled) {
		this.settled = settled;
	}

	public Long getTotal() {
		return orderItems.stream().mapToLong(OrderItem::getTotal).sum();
	}

	public Boolean getSettled() {
		return settled;
	}

	public void setSettled(Boolean settled) {
		this.settled = settled;
	}

	public EPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(EPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getStripeChagreId() {
		return stripeChagreId;
	}

	public void setStripeChagreId(String stripeChagreId) {
		this.stripeChagreId = stripeChagreId;
	}

}
