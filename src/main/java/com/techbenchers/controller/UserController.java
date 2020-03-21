package com.techbenchers.controller;

import com.techbenchers.database.UserRepository;
import com.techbenchers.service.UserService;
import com.techbenchers.type.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    /**
     * End Point which returns logged in user's information
     *
     * @param principal Object received after OAuth2 login
     * @return User object
     */
    @GetMapping("/getUserinfo")
    @ResponseBody
    public User user(Principal principal) {

        return userService.processUserData(principal);
    }

    /**
     * End Point which returns user database
     *
     * @return List of Users
     */
    @GetMapping("/getAllUser")
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

}
