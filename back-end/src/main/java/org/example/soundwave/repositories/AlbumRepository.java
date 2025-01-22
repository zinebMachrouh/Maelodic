package org.example.soundwave.repositories;

import org.example.soundwave.entities.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlbumRepository extends MongoRepository<Album, String> {
    Page<Album> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Album> findByArtistContainingIgnoreCase(String artist, Pageable pageable);

    Page<Album> findByYear(int year, Pageable pageable);
}
