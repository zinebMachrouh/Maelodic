package org.example.soundwave.services;

import org.example.soundwave.dto.RoleDTO;
import org.example.soundwave.dto.UserDTO;
import org.example.soundwave.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    User updateUserRole(String username, List<RoleDTO> roles);
    List<UserDTO> findAllUsers();
}
