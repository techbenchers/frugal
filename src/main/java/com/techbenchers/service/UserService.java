package com.techbenchers.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.techbenchers.database.UserRepository;
import com.techbenchers.type.User;
import com.techbenchers.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private User user;

    private JsonUtil jsonUtil = new JsonUtil();

    public User processUserData(Principal principal) {
        try {
            user = new User();
            JsonNode jsonNode = getJsonObject(principal);
            setUserObject(jsonNode);
            updateUserDatabase();
            System.out.println("User Logged in successfully.. Id: " + user.getId() + " Name: " + user.getName() + " Email: " + user.getEmail());
            return user;
        } catch (Exception e) {
            System.out.println("Exception in processUserData: " + e.toString());
        }

        return null;
    }

    private JsonNode getJsonObject(Principal principal) throws Exception {
        try {
            String json = jsonUtil.objectToJsonString(principal);
            return jsonUtil.jsonStringToJsonObject(json);

        } catch (Exception e) {
            System.out.println("Exception in getJsonObject: " + e.toString());
            throw e;
        }


    }

    private void setUserObject(JsonNode jsonNode) throws Exception {
        try {
            user.setName(jsonUtil.getFieldValue(jsonNode, "name"));
            user.setEmail(jsonUtil.getFieldValue(jsonNode, "email"));
            user.setId(jsonUtil.getFieldValue(jsonNode, "sub"));
        } catch (Exception e) {
            System.out.println("Exception in setUserObject: " + e.toString());
        }

    }

    private void updateUserDatabase() {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Exception in updateUserDatabase: " + e.toString());
        }
    }
}
