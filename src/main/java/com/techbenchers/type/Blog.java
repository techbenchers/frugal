package com.techbenchers.type;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import nonapi.io.github.classgraph.json.Id;

@Component
@Document
public class Blog {
	@Id
	private String id;
	@NotNull(message = "Title can not be null")
	private String title;
	private String uri;
	private String body;
	private String userId;
	private String createdAt;
	private Boolean isAnonymous;
	private Boolean isPublished;
	private String updatedAt;
	private String publishedAt;
	private String titleImage;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(Boolean anonymous) {
		isAnonymous = anonymous;
	}

	public Boolean getPublished() {
		return isPublished;
	}

	public void setPublished(Boolean published) {
		isPublished = published;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(String publishedAt) {
		this.publishedAt = publishedAt;
	}

	public String getTitleImage() {
		return titleImage;
	}

	public void setTitleImage(String titleImage) {
		this.titleImage = titleImage;
	}
}
