package com.congnghejava.webbanhang.payload.response;

import java.text.SimpleDateFormat;

import com.congnghejava.webbanhang.models.Review;

public class ReviewResponse {
	private long id;
	private float rating;
	private String time;
	private String userName;
	private String comment;
	private String avatarUrl;

	public ReviewResponse(Review review) {
		this.id = review.getId();
		this.rating = review.getRating();
		this.userName = review.getUser().getName();
		this.comment = review.getComment();
		this.time = new SimpleDateFormat("dd/MM/yyyy").format(review.getTime()).toString();
		this.avatarUrl = review.getUser().getAvatarUrl();
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
