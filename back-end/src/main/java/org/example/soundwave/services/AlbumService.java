package org.example.soundwave.services;

import org.example.soundwave.dto.AlbumDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlbumService {
    Page<AlbumDTO> listAlbums(Pageable pageable);

    Page<AlbumDTO> searchAlbumsByTitle(String title, Pageable pageable);

    Page<AlbumDTO> searchAlbumsByArtist(String artist, Pageable pageable);

    Page<AlbumDTO> filterAlbumsByYear(int year, Pageable pageable);

    AlbumDTO addAlbum(AlbumDTO albumDTO);

    AlbumDTO updateAlbum(String id, AlbumDTO albumDTO);

    void deleteAlbum(String id);
}
