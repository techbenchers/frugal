package com.techbenchers.controller;

import static com.techbenchers.util.ValidationUtil.Null;

import java.util.List;

import javax.validation.Valid;

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
			if (Null(userId)) throw new Exception("Invalid user ID");
			return blogService.getUserBlogs(userService.getUserBlogIds(userId));
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
	public Blog addBlog(@Valid @RequestBody Blog blog) {
		try {
			if (Null(blog.getTitle()) || blog.getId() != null || !Null(blog.getUserId()) || !Null(blog.getCreatedAt()) || !Null(blog.getUpdatedAt()) || !Null(blog.getUri())) {
				throw new Exception("Invalid request object");
			}
			blogService.addBlog(blog);
			userService.updateUserBlog(blog);
			return blog;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	/**
	 * @param blog Blog to be updated
	 * @return Updated Blog
	 */
	@PostMapping("/update")
	public Blog updateBlog(@RequestBody Blog blog) {
		try {
			if (Null(blog.getId(), blog.getUserId(), blog.getCreatedAt(), blog.getUpdatedAt(), blog.getUri()) || !userService.isLoggedInUser(blog.getUserId())) {
				throw new Exception("Invalid request object");
			}
			if (userService.userHasBlog(blog.getId())) {
				blogService.updateBlog(blog);
				return blog;
			}
			throw new Exception("Invalid blog");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
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
			if(Null(id)) throw new Exception("Invalid blog ID");
			return blogService.getBlogById(id);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	/**
	 * @param id id of blog
	 * @return Message after successful removal of  blog
	 */
	@DeleteMapping("/remove/{id}")
	public String removeBlog(@PathVariable(value = "id") String id) {
		try {
			if (Null(id)) {
				throw new Exception("Invalid id");
			}
			if (userService.userHasBlog(id)) {
				blogService.deleteBlogById(id);
				userService.deleteUserBlog(id);
				return "Deleted blog successfully";
			}
			throw new Exception("User doesn't have blog " + id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

//	@GetMapping("/blog/topK")
//	public List<Blog> getTopKBlogs() {
//		return blogRepository.
//	}


}
