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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AlbumDTO {
    @Builder.Default
    private String id = uuidGenerator.generate();

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String title;

    @NotBlank(message = "Artist is required")
    @Size(min = 3, max = 50, message = "Artist must be between 3 and 50 characters")
    private String artist;

    @NotBlank(message = "Genre is required")
    @Size(min = 3, max = 50, message = "Genre must be between 3 and 50 characters")
    private String genre;

    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be greater than 1900")
    private int year;
}
