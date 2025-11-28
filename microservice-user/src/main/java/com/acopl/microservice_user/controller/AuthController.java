package com.acopl.microservice_user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acopl.microservice_user.model.User;
import com.acopl.microservice_user.repository.UserRepository;
import com.acopl.microservice_user.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String email, @RequestParam String password,
            @RequestParam(required = false, defaultValue = "USER") String rol,
            @RequestParam(required = false) String name) {
        // Check if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        // Hash password and save user
        User u = new User();
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));
        u.setRole(rol);
        u.setName(name != null ? name : email);
        userRepository.save(u);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        User u = userRepository.findByEmail(email).orElse(null);
        if (u == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        // Verify password using BCrypt
        if (!passwordEncoder.matches(password, u.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        String token = jwtUtil.generateToken(u.getId(), u.getEmail());
        return ResponseEntity.ok(Map.of("token", token, "userId", u.getId(), "email", u.getEmail()));
    }
}
