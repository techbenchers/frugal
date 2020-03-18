package com.techbenchers.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techbenchers.util.JsonUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;

@RestController
public class Controller {
    @GetMapping("/user")
    @ResponseBody
    public HashMap<String, String> user(Principal principal) throws JsonProcessingException {
        JsonUtil parseJsonString = new JsonUtil();
        return parseJsonString.parse(principal);
    }
}
