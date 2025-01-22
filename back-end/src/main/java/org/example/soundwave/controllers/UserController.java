package org.example.soundwave.controllers;

import lombok.AllArgsConstructor;
import org.example.soundwave.dto.RoleDTO;
import org.example.soundwave.dto.UserDTO;
import org.example.soundwave.entities.User;
import org.example.soundwave.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<UserDTO> users = userService.findAllUsers();
        System.out.println("find all");
        if (users.isEmpty()) {
            return ResponseEntity.status(404).body("No users found");
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/find/{username}")
    public ResponseEntity<?> findUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        System.out.println("find by username");
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        System.out.println("User not found");
        return ResponseEntity.status(404).body("User not found");
    }

    @PutMapping("/users/roles/{username}")
    public ResponseEntity<?> updateUserRole(@PathVariable String username, @RequestBody List<RoleDTO> roles) {
        try {
            User user = userService.updateUserRole(username, roles);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user roles: " + e.getMessage());
        }
    }
}
