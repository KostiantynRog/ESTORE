package com.rog.EShop.controllers;

import com.rog.EShop.dto.UserDto;
import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/users/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping("/users/register")
    public UserDto registerUser(@RequestBody UserRegisterDto userRegisterDto) {
        return userService.save(userRegisterDto);
    }

}
