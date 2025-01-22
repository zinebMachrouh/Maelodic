package org.example.soundwave.services.impl;

import org.example.soundwave.dto.AlbumDTO;
import org.example.soundwave.entities.Album;
import org.example.soundwave.repositories.AlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AlbumServiceImplTest {

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private AlbumServiceImpl albumService;

    private AlbumDTO albumDTO;

    private Album album;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        albumDTO = AlbumDTO.builder()
                .id("1")
                .title("Test Album")
                .artist("Test Artist")
                .genre("Rock")
                .year(2020)
                .build();

        album = Album.builder()
                .id("1")
                .title("Test Album")
                .artist("Test Artist")
                .genre("Rock")
                .year(2020)
                .build();
    }

    @Test
    @DisplayName("Test listing albums")
    void testListAlbums() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Album> albumPage = new PageImpl<>(List.of(album));
        when(albumRepository.findAll(pageable)).thenReturn(albumPage);

        Page<AlbumDTO> result = albumService.listAlbums(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Album", result.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("Test searching albums by title")
    void testSearchAlbumsByTitle() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Album> albumPage = new PageImpl<>(List.of(album));
        when(albumRepository.findByTitleContainingIgnoreCase("Test", pageable)).thenReturn(albumPage);

        Page<AlbumDTO> result = albumService.searchAlbumsByTitle("Test", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Album", result.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("Test searching albums by artist")
    void testSearchAlbumsByArtist() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Album> albumPage = new PageImpl<>(List.of(album));
        when(albumRepository.findByArtistContainingIgnoreCase("Test Artist", pageable)).thenReturn(albumPage);

        Page<AlbumDTO> result = albumService.searchAlbumsByArtist("Test Artist", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Artist", result.getContent().get(0).getArtist());
    }

    @Test
    @DisplayName("Test filtering albums by year")
    void testFilterAlbumsByYear() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Album> albumPage = new PageImpl<>(List.of(album));
        when(albumRepository.findByYear(2020, pageable)).thenReturn(albumPage);

        Page<AlbumDTO> result = albumService.filterAlbumsByYear(2020, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(2020, result.getContent().get(0).getYear());
    }

    @Test
    @DisplayName("Test adding an album")
    void testAddAlbum() {
        when(albumRepository.save(any(Album.class))).thenReturn(album);

        AlbumDTO result = albumService.addAlbum(albumDTO);

        assertNotNull(result);
        assertEquals("Test Album", result.getTitle());
        verify(albumRepository, times(1)).save(any(Album.class));
    }

    @Test
    @DisplayName("Test updating an existing album")
    void testUpdateAlbum() {
        when(albumRepository.findById("1")).thenReturn(Optional.of(album));
        when(albumRepository.save(any(Album.class))).thenReturn(album);

        AlbumDTO result = albumService.updateAlbum("1", albumDTO);

        assertNotNull(result);
        assertEquals("Test Album", result.getTitle());
        verify(albumRepository, times(1)).findById("1");
        verify(albumRepository, times(1)).save(any(Album.class));
    }

    @Test
    @DisplayName("Test updating a non-existing album throws exception")
    void testUpdateAlbumNotFound() {
        when(albumRepository.findById("1")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            albumService.updateAlbum("1", albumDTO);
        });
        assertEquals("Album not found", exception.getMessage());
        verify(albumRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Test deleting an album")
    void testDeleteAlbum() {
        when(albumRepository.existsById("1")).thenReturn(true);

        albumService.deleteAlbum("1");

        verify(albumRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Test deleting a non-existing album throws exception")
    void testDeleteAlbumNotFound() {
        when(albumRepository.existsById("1")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            albumService.deleteAlbum("1");
        });
        assertEquals("Album not found", exception.getMessage());
        verify(albumRepository, times(0)).deleteById("1");
    }
}
