package com.techbenchers.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.techbenchers.database.UserRepository;
import com.techbenchers.type.Blog;
import com.techbenchers.type.Constants;
import com.techbenchers.type.User;
import com.techbenchers.util.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private User user;

//	public boolean isUserPresent(Principal principal) throws Exception {
//		JsonNode jsonNode = getJsonObject(principal);
//		String id = JsonUtil.getFieldValue(jsonNode, Constants.USER_ID_KEY_GITHUB)
//		return user.getId() != null;
//	}
//
//	public User getUser() {
//		return user;
//	}

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
			setUserID(jsonNode, Constants.USER_ID_KEY_GOOGLE);
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

	private void setUserID(@NotNull JsonNode jsonNode, @NotNull String idKey) {
		try {
			user.setId(JsonUtil.getFieldValue(jsonNode, idKey));
		} catch (Exception e) {
			System.out.println("Exception in setUserID: " + e.toString());
			setGitHubUserID(jsonNode, Constants.USER_ID_KEY_GITHUB);
		}
	}

	private void setGitHubUserID(@NotNull JsonNode jsonNode, @NotNull String idKey) {
		try {
			user.setId(JsonUtil.getFieldValue(jsonNode, idKey));
		} catch (Exception e) {
			System.out.println("Exception in setGitHubUserID: " + e.toString());
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
			Optional<User> userFromDatabase = userRepository.findById(user.getId());
			if (userFromDatabase.isPresent()) {
				if (checkAdminStatus(userFromDatabase.get())) {
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
			userRepository.save(user);

		} catch (Exception e) {
			System.out.println("Exception in updateUserBlog " + e.toString());
			throw e;
		}
	}

	public void deleteUserBlog(String id) throws Exception {
		try {
			user.removeBlogId(id);
			userRepository.save(user);
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

}
