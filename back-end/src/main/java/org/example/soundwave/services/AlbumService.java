package org.example.soundwave.services;

import org.example.soundwave.dto.AlbumDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlbumService {
    Page<AlbumDTO> listAlbums(Pageable pageable);

    List<AlbumDTO> listAllAlbum();

    Page<AlbumDTO> searchAlbumsByTitle(String title, Pageable pageable);

    Page<AlbumDTO> searchAlbumsByArtist(String artist, Pageable pageable);

    Page<AlbumDTO> filterAlbumsByYear(int year, Pageable pageable);

    AlbumDTO addAlbum(AlbumDTO albumDTO);

    AlbumDTO updateAlbum(String id, AlbumDTO albumDTO);

    void deleteAlbum(String id);
}
