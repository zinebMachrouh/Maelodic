package org.example.soundwave.repositories;

import org.example.soundwave.entities.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SongRepository extends MongoRepository<Song, String> {
    Page<Song> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Song> findByAlbum_Id(String albumId, Pageable pageable);
}
