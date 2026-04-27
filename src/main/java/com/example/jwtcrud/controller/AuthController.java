package com.example.jwtcrud.controller;

import com.example.jwtcrud.config.JwtUtil;
import com.example.jwtcrud.dto.AuthRequest;
import com.example.jwtcrud.dto.AuthResponse;
import com.example.jwtcrud.model.User;
import com.example.jwtcrud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request){
        if (userService.findByUsername(request.getUsername()) != null){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username ya existe");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.createUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User created" + user.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = userService.findByUsername(request.getUsername());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

}
