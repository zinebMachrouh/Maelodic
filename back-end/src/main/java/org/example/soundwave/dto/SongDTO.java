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
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SongDTO {
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

    private LocalDateTime addedDate;

    @NotNull(message = "category is required")
    @Size(min = 3, max = 50, message = "category must be between 3 and 50 characters")
    private String category;

    private Long duration;

    @NotBlank(message = "Album is required")
    private String albumId;

    private String audioUrl;
    private String imageUrl;
}
