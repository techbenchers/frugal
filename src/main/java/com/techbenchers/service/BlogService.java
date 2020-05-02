package com.techbenchers.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techbenchers.database.BlogRepository;
import com.techbenchers.database.UserRepository;
import com.techbenchers.type.Blog;
import com.techbenchers.type.User;
import com.techbenchers.util.UriUtil;

@Component
public class BlogService {

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private UserRepository userRepository;

	public Blog upsertBlog(@NotNull Blog blog) throws Exception {
		try {
			blogRepository.save(blog);
			setBlogUri(blog);
			blogRepository.save(blog);
			System.out.println("blog turi is " + blog.getUri());
			return blog;
		} catch (Exception e) {
			System.out.println("Exception in upsertBlog: " + e.toString());
			throw e;
		}
	}

	private void setBlogUri(@NotNull Blog blog) throws Exception {
		try {
			final String uri = UriUtil.uriGenerator(blog.getTitle(), blog.getId());
			blog.setUri(uri);
		} catch (Exception e) {
			System.out.println("Exception in setBlogUri " + e.toString());
			throw e;
		}
	}

	public List<Blog> getUserBlogs(@NotNull String userId) throws Exception {
		try {
			User user = userRepository.findById(userId).orElse(null);
			if (user == null) {
				throw new Exception("User not found");
			}
			List<String> blogIds = user.getBlogIds();
			List<Blog> blogs = new ArrayList<Blog>();
			for (String blogId : blogIds) {
				blogRepository.findById(blogId).ifPresent(blogs::add);
			}
			return blogs;
		} catch (Exception e) {
			System.out.println("Exception in getUserBlogs" + e.toString());
			throw e;
		}

	}

	public List<Blog> getAllBlogs() throws Exception {
		try {
			return blogRepository.findAll();
		} catch (Exception e) {
			System.out.println("Exception is getAllBlogs " + e.toString());
			throw e;
		}
	}

	public Blog getBlogById(String id) throws Exception {
		try {
			return blogRepository.findById(id).orElse(null);
		} catch (Exception e) {
			System.out.println("Exception is getBlogById " + e.toString());
			throw e;
		}
	}

	public String deleteBlogById(String id) throws Exception {
		try {
			blogRepository.deleteById(id);
			return "Blog deleted  successfully";
		} catch (Exception e) {
			System.out.println("Exception is deleteBlogById " + e.toString());
			throw e;
		}
	}
}
