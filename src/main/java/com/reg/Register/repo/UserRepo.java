package com.reg.Register.repo;

import com.reg.Register.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findBySessionId(String sessionId);

    Optional<User> findByEmailAndSessionId(String email, String sessionId);
}
