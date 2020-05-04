package com.techbenchers.controller;

import com.techbenchers.service.UserService;
import com.techbenchers.type.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * End Point which returns logged in user's information
	 *
	 * @param principal Object received after OAuth2 login
	 * @return User object
	 */
	@GetMapping("/info")
	public User user(Principal principal) {
		try {
			User user = userService.isUserPresent(principal);
			if (user != null) {
				return user;
			}
			return userService.processUserData(principal);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

	// Todo: Remove below method

	/**
	 * End Point which returns user database
	 *
	 * @return List of Users
	 */
	@GetMapping("/all")
	public List<User> getAllUsers() {
		try {
			return userService.getAllUsers();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

	// Todo: Remove below method

	/**
	 * @return Message after successful removal of all users
	 */
	@GetMapping("/removeAll")
	public String removeAllUsersData() {
		try {
			return userService.deleteAllUsers();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

}
