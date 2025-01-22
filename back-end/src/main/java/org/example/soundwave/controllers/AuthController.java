package org.example.soundwave.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.soundwave.dto.LoginRequest;
import org.example.soundwave.dto.LoginResponse;
import org.example.soundwave.dto.UserDTO;
import org.example.soundwave.entities.Role;
import org.example.soundwave.entities.User;
import org.example.soundwave.repositories.RoleRepository;
import org.example.soundwave.repositories.UserRepository;
import org.example.soundwave.utils.JwtUtil;
import org.example.soundwave.utils.TokenBlacklist;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Profile("prod")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpSession httpSession) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        claims.put("userId", user.getId());

        String token = jwtUtil.generateToken(user.getUsername(), claims);
        httpSession.setAttribute("token", token);

        return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), user.getRoles()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO registrationRequest) {
        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        List<String> requestedRoles = registrationRequest.getRoles();
        List<Role> validRoles = roleRepository.findAll();

        List<Role> rolesToAssign = requestedRoles.stream()
                .map(roleName -> validRoles.stream()
                        .filter(role -> role.getName().equals(roleName))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName))
                )
                .toList();

        User newUser = User.builder()
                .username(registrationRequest.getUsername())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .active(true)
                .roles(rolesToAssign)
                .build();

        User savedUser = userRepository.save(newUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", savedUser.getRoles());
        claims.put("userId", savedUser.getId());
        String token = jwtUtil.generateToken(savedUser.getUsername(), claims);


        return ResponseEntity.ok(new LoginResponse(token, savedUser.getUsername(), savedUser.getRoles()));
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession httpSession) {
        httpSession.getAttribute("token");
        TokenBlacklist.add((String) httpSession.getAttribute("token"));

        System.out.println("Blacklist After Add: ");
        System.out.println(TokenBlacklist.getBlacklist());

        TokenBlacklist.clean();

        System.out.println("Blacklist After Clean: ");
        System.out.println(TokenBlacklist.getBlacklist());

        httpSession.invalidate();

        return ResponseEntity.ok("Logged out successfully");
    }
}