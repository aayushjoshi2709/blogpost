package com.example.blogpost.utils;

import com.example.blogpost.model.User;
import com.example.blogpost.model.UserPrincipal;
import com.example.blogpost.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    private final UserService userService;

    public Utils(UserService userService) {
        this.userService = userService;
    }

    public User getUserFromSecurityContext(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        return userService.findByUsername(userPrincipal.getUsername());
    }
}
