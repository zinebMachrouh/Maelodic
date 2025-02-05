package org.example.soundwave.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.soundwave.utils.uuidGenerator;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SongRequestDTO {
    @Builder.Default
    private String id = uuidGenerator.generate();

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String title;

    @NotNull(message = "Track number is required")
    @Min(value = 1, message = "Track number must be greater than 0")
    private int trackNumber;

    @NotNull(message = "description is required")
    @Size(min = 3, max = 100, message = "description must be between 3 and 100 characters")
    private String description;

    @NotNull(message = "category is required")
    @Size(min = 3, max = 50, message = "category must be between 3 and 50 characters")
    private String category;

    @NotBlank(message = "Album is required")
    private String albumId;

    private MultipartFile audioUrl;
    private MultipartFile imageUrl;
}
