package com.techbenchers.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.techbenchers.database.UserRepository;
import com.techbenchers.type.Constants;
import com.techbenchers.type.User;
import com.techbenchers.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

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
            updateAdminStatus();
            updateUserDatabase();
            System.out.println("User Logged in successfully.. Id: " + user.getId() + " Email: " + user.getEmail() + " Admin: " + user.isAdmin());
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
            user.setName(jsonUtil.getFieldValue(jsonNode, Constants.USER_NAME_KEY));
            user.setEmail(jsonUtil.getFieldValue(jsonNode, Constants.USER_EMAIL_KEY));
            setUserID(jsonNode, Constants.USER_ID_KEY_GOOGLE);
        } catch (Exception e) {
            System.out.println("Exception in setUserObject: " + e.toString());
        }

    }

    private void setUserID(JsonNode jsonNode, String idKey) {

        try {
            user.setId(jsonUtil.getFieldValue(jsonNode, idKey));
        } catch (Exception e) {
            System.out.println("Exception in setUserID: " + e.toString());
            setUserID(jsonNode, Constants.USER_ID_KEY_GITHUB);
        }
    }

    private void updateUserDatabase() {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Exception in updateUserDatabase: " + e.toString());
        }
    }

    private void updateAdminStatus() {

        try {
            Optional<User> temp = userRepository.findById(user.getId());
            if (temp.isPresent()) {
                if (checkAdminStatus(temp.get())) {
                    user.setAdmin(true);
                }
            } else {
                user.setAdmin(false);
            }
        } catch (Exception e) {
            System.out.println("Exception in updateAdminStatus: " + e.toString());
        }
    }

    private boolean checkAdminStatus(User temp) {
        return temp.isAdmin();
    }

}
