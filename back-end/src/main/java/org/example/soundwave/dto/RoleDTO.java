package org.example.soundwave.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.soundwave.utils.uuidGenerator;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoleDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;
}
