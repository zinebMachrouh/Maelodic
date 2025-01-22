package org.example.soundwave.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.soundwave.entities.Role;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginResponse {
    String token;
    String username;
    List<Role> roles;
}
