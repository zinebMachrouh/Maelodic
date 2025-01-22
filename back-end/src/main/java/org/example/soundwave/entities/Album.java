package org.example.soundwave.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "albums")
public class Album {
    @Id
    private String id;

    private String title;

    private String artist;

    private String genre;

    private int year;

    @DocumentReference(lazy = true)
    private List<Song> songs;
}
