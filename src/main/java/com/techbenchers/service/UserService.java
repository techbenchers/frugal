package com.techbenchers.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.techbenchers.database.UserRepository;
import com.techbenchers.type.Constants;
import com.techbenchers.type.User;
import com.techbenchers.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private User user;

    public User processUserData(@NotNull Principal principal) {
        try {
            JsonNode jsonNode = getJsonObject(principal);
            setUserObject(jsonNode);
            updateAdminStatus();
            updateUserDatabase();
            System.out.println("User Logged in successfully with Id: " + user.getId() + " Email: " + user.getEmail() + " Admin: " + user.isAdmin());
        } catch (Exception e) {
            System.out.println("Exception in processUserData: " + e.toString());
        }
        return user;
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
        } catch (Exception e) {
            System.out.println("Exception in setUserObject: " + e.toString());
            throw e;
        }

    }

    private void setUserName(@NotNull JsonNode jsonNode) throws Exception {
        user.setName(JsonUtil.getFieldValue(jsonNode, Constants.USER_NAME_KEY));
    }

    private void setUserEmail(JsonNode jsonNode) throws Exception {
        user.setEmail(JsonUtil.getFieldValue(jsonNode, Constants.USER_EMAIL_KEY));
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

    private void updateUserDatabase() {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Exception in updateUserDatabase: " + e.toString());
        }
    }

    private void updateAdminStatus() {
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
        }
    }

    private boolean checkAdminStatus(@NotNull User temp) {
        return temp.isAdmin();
    }

}
