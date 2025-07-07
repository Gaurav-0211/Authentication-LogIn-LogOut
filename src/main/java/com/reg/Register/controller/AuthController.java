package com.reg.Register.controller;

import com.reg.Register.dto.UserDto;
import com.reg.Register.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/register")
    public String register(@RequestBody UserDto userDto){
        log.info("AuthController register ");
        return authService.registerUser(userDto);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserDto userDto){
        log.info("AuthController login");
        return authService.loginUser(userDto);
    }

    @PostMapping("/logout")
    public String logout(@RequestBody Map<String, String> logoutRequest) {
        String email = logoutRequest.get("email");
        String sessionId = logoutRequest.get("sessionId");
        return authService.logoutUser(email, sessionId);
    }
}
