package org.example.soundwave.services.impl;

import lombok.AllArgsConstructor;
import org.example.soundwave.dto.AlbumDTO;
import org.example.soundwave.entities.Album;
import org.example.soundwave.repositories.AlbumRepository;
import org.example.soundwave.services.AlbumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;

    @Override
    public Page<AlbumDTO> listAlbums(Pageable pageable) {
        return albumRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<AlbumDTO> searchAlbumsByTitle(String title, Pageable pageable) {
        return albumRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<AlbumDTO> searchAlbumsByArtist(String artist, Pageable pageable) {
        return albumRepository.findByArtistContainingIgnoreCase(artist, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<AlbumDTO> filterAlbumsByYear(int year, Pageable pageable) {
        return albumRepository.findByYear(year, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public AlbumDTO addAlbum(AlbumDTO albumDTO) {
        Album album = mapToEntity(albumDTO);
        Album savedAlbum = albumRepository.save(album);
        return mapToDTO(savedAlbum);
    }

    @Override
    public AlbumDTO updateAlbum(String id, AlbumDTO albumDTO) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        // Update the fields from DTO
        album.setTitle(albumDTO.getTitle());
        album.setArtist(albumDTO.getArtist());
        album.setGenre(albumDTO.getGenre());
        album.setYear(albumDTO.getYear());

        Album updatedAlbum = albumRepository.save(album);
        return mapToDTO(updatedAlbum);
    }

    @Override
    public void deleteAlbum(String id) {
        if (!albumRepository.existsById(id)) {
            throw new IllegalArgumentException("Album not found");
        }
        albumRepository.deleteById(id);
    }

    private AlbumDTO mapToDTO(Album album) {
        return AlbumDTO.builder()
                .id(album.getId())
                .title(album.getTitle())
                .artist(album.getArtist())
                .genre(album.getGenre())
                .year(album.getYear())
                .build();
    }

    private Album mapToEntity(AlbumDTO album) {
        return Album.builder()
                .id(album.getId())
                .title(album.getTitle())
                .artist(album.getArtist())
                .genre(album.getGenre())
                .year(album.getYear())
                .build();
    }
}
