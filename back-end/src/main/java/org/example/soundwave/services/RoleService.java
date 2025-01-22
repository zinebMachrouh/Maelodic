package org.example.soundwave.services;

import org.example.soundwave.entities.Role;

public interface RoleService {
    Role findByName(String name);
    Role saveRole(Role role);
}
