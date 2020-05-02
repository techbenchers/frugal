package com.techbenchers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.techbenchers.database.BlogRepository;
import com.techbenchers.service.BlogService;
import com.techbenchers.service.UserService;
import com.techbenchers.type.Blog;

@RestController
@RequestMapping("/blog")
public class BlogController {

	@Autowired
	private BlogService blogService;

	@Autowired
	private UserService userService;


	/**
	 * @param userId Id of user
	 * @return List of Blogs of given user id
	 */
	@GetMapping("/user/{userId}")
	public List<Blog> getUserBlogs(@PathVariable(value = "userId") String userId) {
		try {
			List<Blog> blogs = blogService.getUserBlogs(userId);
			return blogs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

	/**
	 * @param blog Blog to be created
	 * @return Blog
	 */
	@PostMapping("/add")
	public Blog addBlog(@RequestBody Blog blog) {
		try {
			blogService.upsertBlog(blog);
			userService.updateUserBlog(blog);
			return blog;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

	/**
	 * @param blog Blog to be updated
	 * @return Updated Blog
	 */
	@PostMapping("/update")
	public Blog upsertBlog(@RequestBody Blog blog) {
		try {
			blogService.upsertBlog(blog);
			return blog;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

	/**
	 * @return List of all blogs
	 */
	@GetMapping("/all")
	public List<Blog> getAllBLog() {
		try {
			return blogService.getAllBlogs();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

	/**
	 * @param id id of blog
	 * @return Blog
	 */
	@GetMapping("/{id}")
	public Blog getBlog(@PathVariable(value = "id") String id) {
		try {
			return blogService.getBlogById(id);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

	/**
	 * @param id id of blog
	 * @return Message after successful deletion
	 */
	@DeleteMapping("/remove/{id}")
	public String removeBlog(@PathVariable(value = "id") String id) {
		try {
			blogService.deleteBlogById(id);
			userService.deleteUserBlog(id);
			return "Deleted blog successfully";
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

//	@GetMapping("/blog/topK")
//	public List<Blog> getTopKBlogs() {
//		return blogRepository.
//	}


}
