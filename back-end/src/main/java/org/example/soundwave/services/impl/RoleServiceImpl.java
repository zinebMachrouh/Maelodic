package org.example.soundwave.services.impl;

import lombok.AllArgsConstructor;
import org.example.soundwave.entities.Role;
import org.example.soundwave.repositories.RoleRepository;
import org.example.soundwave.services.RoleService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
