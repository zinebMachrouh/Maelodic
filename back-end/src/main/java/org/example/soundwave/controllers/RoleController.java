package org.example.soundwave.controllers;

import lombok.AllArgsConstructor;
import org.example.soundwave.dto.RoleDTO;
import org.example.soundwave.entities.Role;
import org.example.soundwave.services.RoleService;
import org.example.soundwave.utils.uuidGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody RoleDTO roleDTO) {
        Role role = Role.builder()
                .id(uuidGenerator.generate())
                .name(roleDTO.getName())
                .build();
        roleService.saveRole(role);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getRoleByName(@PathVariable String name) {
        Role role = roleService.findByName(name);
        if (role == null) {
            return ResponseEntity.badRequest().body("Role not found");
        }else {
            return ResponseEntity.ok(role);
        }
    }
}
