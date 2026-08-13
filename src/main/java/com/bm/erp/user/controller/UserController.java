package com.bm.erp.user.controller;

import com.bm.erp.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable UUID id, Authentication authentication) {
         userService.delete(id);
    }
}
