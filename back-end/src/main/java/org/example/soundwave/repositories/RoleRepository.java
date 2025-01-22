package org.example.soundwave.repositories;

import org.example.soundwave.entities.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByName(String name);

    List<Role> findByNameIn(List<String> roleNames);
}
