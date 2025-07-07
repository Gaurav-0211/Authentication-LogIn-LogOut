package com.reg.Register.service;

import com.reg.Register.dto.UserDto;

import java.util.Map;

public interface AuthService {
    String registerUser(UserDto userDto);
    Map<String, String> loginUser(UserDto userDto);
    String logoutUser(String email,String sessionId);
}
