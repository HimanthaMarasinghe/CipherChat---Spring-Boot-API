package com.CipherChat.CipherChat.Controllers;

import com.CipherChat.CipherChat.Models.User;
import com.CipherChat.CipherChat.Repositories.UserRepository;
import com.CipherChat.CipherChat.Utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.Name;

@RestController
public class UserController {

    @Autowired
    UserRepository UserRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/newUser")
    public User CreateUser(@RequestBody User user) {
        return UserRepo.save(user);
    }

    @PostMapping("/login")
    public String Login(@RequestBody User user) {
        User existingUser = UserRepo.findByName(user.getName());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            return String.format("{\"token\":\"%s\", \"userId\":\"%s\"}", jwtUtil.generateToken(user.getName()), existingUser.getUserId());
        } else {
            return "Invalid credentials";
        }
    }
}
