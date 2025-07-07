package com.reg.Register.service.Impl;

import com.reg.Register.dto.UserDto;
import com.reg.Register.model.User;
import com.reg.Register.repo.UserRepo;
import com.reg.Register.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepo userRepo;

    private final int MAX_ATTEMPT = 3;
    private final int LOCK_TIME = 5;

    @Override
    public String registerUser(UserDto userDto) {
        if(userRepo.findByEmail(userDto.getEmail()).isPresent()) {
            return "Email Already Registered";
        }
        log.info("AuthServiceImpl register method");
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword((userDto.getPassword()));
        userRepo.save(user);
        return "User Registered Successfully";
    }

    @Override
    public Map<String, String> loginUser(UserDto userDto) {
        Map<String, String> response = new HashMap<>();
        Optional<User> optionalUser = userRepo.findByEmail(userDto.getEmail());
        if(optionalUser.isEmpty()){
            response.put("error", "Invalid Credentials");
            return response;
        }
        User user = optionalUser.get();

        if(user.getLockTime() != null && LocalDateTime.now().isBefore(user.getLockTime())){
            response.put("error", "Account Locked. Please Try Later");
            return response;
        }

        if(!user.getPassword().equals(userDto.getPassword())){
            user.setFailedAttempts(user.getFailedAttempts() + 1);
            if(user.getFailedAttempts() >= MAX_ATTEMPT){
                user.setLockTime(LocalDateTime.now().plusMinutes(LOCK_TIME));
            }
            userRepo.save(user);
            response.put("error", "Invalid Credentials");
            return response;
        }
        log.info("AuthServiceImpl login method");
        user.setFailedAttempts(0);
        user.setLockTime(null);
        String sessionId = UUID.randomUUID().toString();
        user.setSessionId(sessionId);
        userRepo.save(user);
        response.put("message", "Login Successfully");
        response.put("sessionId", sessionId);
        return response;
    }

    public String logoutUser(String email, String sessionId){
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if(optionalUser.isEmpty()){
            return "User Not Found";
        }
        User user = optionalUser.get();
        if(user.getSessionId() == null || !user.getSessionId().equals(sessionId)){
            return "Invalid session or user already logged out ";
        }
        user.setSessionId(null);
        userRepo.save(user);
        return "Logged out Successfully";
    }
}
