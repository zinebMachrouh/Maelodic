package org.example.soundwave.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "songs")
public class Song {
    @Id
    private String id;

    private String title;

    private Integer trackNumber;

    private String description;

    private LocalDateTime addedDate;

    private String category;

    private Long duration;

    private String audioUrl;

    private String imageUrl;

    @DocumentReference(lazy = true)
    private Album album;
}
