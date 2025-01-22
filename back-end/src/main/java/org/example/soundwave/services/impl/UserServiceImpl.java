package org.example.soundwave.services.impl;

import lombok.AllArgsConstructor;
import org.example.soundwave.dto.RoleDTO;
import org.example.soundwave.dto.UserDTO;
import org.example.soundwave.entities.Role;
import org.example.soundwave.entities.User;
import org.example.soundwave.repositories.RoleRepository;
import org.example.soundwave.repositories.UserRepository;
import org.example.soundwave.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public User updateUserRole(String username, List<RoleDTO> roles) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }

        User user = userOptional.get();

        List<String> roleNames = roles.stream()
                .map(RoleDTO::getName)
                .toList();

        List<Role> existingRoles = roleRepository.findByNameIn(roleNames);

        if (existingRoles.size() != roleNames.size()) {
            List<String> missingRoles = roleNames.stream()
                    .filter(name -> existingRoles.stream().noneMatch(role -> role.getName().equals(name)))
                    .toList();
            throw new IllegalArgumentException("Invalid roles provided: " + missingRoles);
        }

        user.setRoles(existingRoles);

        return userRepository.save(user);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user ->
                        UserDTO.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .roles(user.getRoles().stream()
                                        .map(Role::getName)
                                        .toList())
                                .build())
                .toList();
    }
}
