package com.techbenchers.type;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@Document
public class User {

	@Id
	private String id;
	private String name;
	@Email
	private String email;
	private boolean admin;
	private List<String> blogIds;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public List<String> getBlogIds() {
		return blogIds;
	}

	public void setBlogIds(List<String> blogIds) {
		this.blogIds = blogIds;
	}

	public void addBlogId(String id) {
		this.blogIds.add(id);
	}

	public void removeBlogId(String id) {
		this.blogIds.remove(id);
	}

	public void copy(User usr) {
		this.id = usr.id;
		this.name = usr.name;
		this.email = usr.email;
		this.admin = usr.admin;
		this.blogIds = usr.blogIds;
	}

}
