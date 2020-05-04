package com.techbenchers.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.techbenchers.database.UserRepository;
import com.techbenchers.type.Blog;
import com.techbenchers.type.Constants;
import com.techbenchers.type.User;
import com.techbenchers.util.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private User user;

	public User isUserPresent(Principal principal) throws Exception {
		try {
			JsonNode jsonNode = getJsonObject(principal);
			String id;
			if (JsonUtil.hasField(jsonNode, Constants.USER_ID_KEY_GOOGLE)) {
				id = JsonUtil.getFieldValue(jsonNode, Constants.USER_ID_KEY_GOOGLE);
			} else {
				id = JsonUtil.getFieldValue(jsonNode, Constants.USER_ID_KEY_GITHUB);
			}
			User tempUser = findUserByID(id);
			if (tempUser != null)
				user.copy(tempUser);
			System.out.println("logged in user isUserPresent " + user.getId() + " " + user.getName());
			return tempUser;
		} catch (Exception e) {
			System.out.println("Exception in isUserPresent " + e.toString());
			throw e;
		}
	}

	public User processUserData(@NotNull Principal principal) throws Exception {
		try {
			JsonNode jsonNode = getJsonObject(principal);
			setUserObject(jsonNode);
			updateAdminStatus();
			updateUserDatabase();
			System.out.println("User Logged in successfully with Id: " + user.getId() + " Email: " + user.getEmail() + " Admin: " + user.isAdmin());
			return user;
		} catch (Exception e) {
			System.out.println("Exception in processUserData: " + e.toString());
			throw e;
		}
	}

	private JsonNode getJsonObject(@NotNull Principal principal) throws Exception {
		try {
			String json = JsonUtil.objectToJsonString(principal);
			return JsonUtil.jsonStringToJsonObject(json);

		} catch (Exception e) {
			System.out.println("Exception in getJsonObject: " + e.toString());
			throw e;
		}
	}

	private void setUserObject(@NotNull JsonNode jsonNode) throws Exception {
		try {
			setUserName(jsonNode);
			setUserEmail(jsonNode);
			setUserID(jsonNode);
			setBlogIds();
		} catch (Exception e) {
			System.out.println("Exception in setUserObject: " + e.toString());
			throw e;
		}

	}

	private void setUserName(@NotNull JsonNode jsonNode) throws Exception {
		try {
			user.setName(JsonUtil.getFieldValue(jsonNode, Constants.USER_NAME_KEY));
		} catch (Exception e) {
			System.out.println("Exception in setUserName " + e.toString());
			throw e;
		}
	}

	private void setUserEmail(JsonNode jsonNode) throws Exception {
		try {
			user.setEmail(JsonUtil.getFieldValue(jsonNode, Constants.USER_EMAIL_KEY));
		} catch (Exception e) {
			System.out.println("Exception in setUserEmail " + e.toString());
			throw e;
		}
	}

	private void setBlogIds() throws Exception {
		user.setBlogIds(new ArrayList<>());
	}

	private void setUserID(@NotNull JsonNode jsonNode) {
		try {
			if (JsonUtil.hasField(jsonNode, Constants.USER_ID_KEY_GOOGLE)) {
				user.setId(JsonUtil.getFieldValue(jsonNode, Constants.USER_ID_KEY_GOOGLE));
			} else {
				user.setId(JsonUtil.getFieldValue(jsonNode, Constants.USER_ID_KEY_GITHUB));
			}
		} catch (Exception e) {
			System.out.println("Exception in setUserID: " + e.toString());
		}
	}

	private void updateUserDatabase() throws Exception {
		try {
			userRepository.save(user);
		} catch (Exception e) {
			System.out.println("Exception in updateUserDatabase: " + e.toString());
			throw e;
		}
	}

	private void updateAdminStatus() throws Exception {
		try {
			User userFromDatabase = findUserByID(user.getId());
			if (userFromDatabase != null) {
				if (checkAdminStatus(userFromDatabase)) {
					user.setAdmin(true);
				}
			} else {
				user.setAdmin(false);
			}
		} catch (Exception e) {
			System.out.println("Exception in updateAdminStatus: " + e.toString());
			throw e;
		}
	}

	private boolean checkAdminStatus(@NotNull User temp) {
		return temp.isAdmin();
	}

	public void updateUserBlog(Blog blog) throws Exception {
		try {
			user.addBlogId(blog.getId());
			updateUserDatabase();
		} catch (Exception e) {
			System.out.println("Exception in updateUserBlog " + e.toString());
			throw e;
		}
	}

	public void deleteUserBlog(String id) throws Exception {
		try {
			user.removeBlogId(id);
			updateUserDatabase();
		} catch (Exception e) {
			System.out.println("Exception is deleteUSerBlog " + e.toString());
			throw e;
		}
	}

	public List<User> getAllUsers() throws Exception {
		try {
			return userRepository.findAll();
		} catch (Exception e) {
			System.out.println("Exception in getAllUsers " + e.toString());
			throw e;
		}
	}

	public String deleteAllUsers() throws Exception {
		try {
			userRepository.deleteAll();
			return "Delted All Users";
		} catch (Exception e) {
			System.out.println("Exception in deleteAllUsers " + e.toString());
			throw e;
		}
	}

	public User findUserByID(String id) {
		try {
			return userRepository.findById(id).orElse(null);
		} catch (Exception e) {
			System.out.println("Exception in findUserByID " + e.toString());
			throw e;
		}
	}

	public boolean userHasBlog(String id) throws Exception {
		try {
			return user.getBlogIds().contains(id);
		} catch (Exception e) {
			System.out.println("Exception in userHasBlog " + e.toString());
			throw e;
		}
	}

	public boolean isLoggedInUser(String id) throws Exception {
		try {
			return id.equals(user.getId());
		} catch (Exception e) {
			System.out.println("Exception in isLoggedInUser " + e.toString());
			throw e;
		}
	}

	public List<String> getUserBlogIds(String userId) throws Exception {
		try {
			return findUserByID(userId).getBlogIds();
		} catch (Exception e) {
			System.out.println("Exception in getUserBlogIds " + e.toString());
			throw e;
		}
	}

}
