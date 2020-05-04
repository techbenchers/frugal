package com.techbenchers.service;

import java.time.Instant;
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
import net.jcip.annotations.NotThreadSafe;


@Component
public class BlogService {

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private User user;

	public void updateBlog(@NotNull Blog blog) throws Exception {
		try {
			upsertBlog(blog);
		} catch (Exception e) {
			System.out.println("Exception in updateBlog: " + e.toString());
			throw e;
		}
	}

	public void addBlog(@NotNull Blog blog) throws Exception {
		try {
			setCreatedAt(blog);
			setUserID(blog);
			updateBlogDatabase(blog);
			upsertBlog(blog);
		} catch (Exception e) {
			System.out.println("Exception in addBlog: " + e.toString());
			throw e;
		}
	}

	private void upsertBlog(@NotNull Blog blog) throws Exception {
		try {
			System.out.println("logged in userID upsertBlog " + user.getId());
			System.out.println("blog in upsertBlog " + blog.getId());
			setUpdatedAt(blog);
			setBlogUri(blog);
			updateBlogDatabase(blog);
			System.out.println("upserted blog is " + blog.toString());
		} catch (Exception e) {
			System.out.println("Exception in upsertBlog: " + e.toString());
			throw e;
		}
	}

	private void setCreatedAt(@NotNull Blog blog) {
		blog.setCreatedAt(getCurrentTime());
	}

	private void setUpdatedAt(@NotNull Blog blog) {
		blog.setUpdatedAt(getCurrentTime());
	}

	private void setUserID(@NotNull Blog blog) {
		blog.setUserId(user.getId());
	}

	private String getCurrentTime() {
		return Instant.now().toString();
	}

	private void updateBlogDatabase(@NotNull Blog blog) throws Exception {
		try {
			blogRepository.save(blog);
		} catch (Exception e) {
			System.out.println("Exception in updateBlogDatabase " + e.toString());
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

	public List<Blog> getUserBlogs(List<String> blogIds) throws Exception {
		try {
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
