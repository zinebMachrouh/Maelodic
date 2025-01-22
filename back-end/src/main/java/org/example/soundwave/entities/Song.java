package org.example.soundwave.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "songs")
public class Song {
    @Id
    private String id;

    private String title;

    private Integer duration;

    private Integer trackNumber;

    @DocumentReference(lazy = true)
    private Album album;
}
