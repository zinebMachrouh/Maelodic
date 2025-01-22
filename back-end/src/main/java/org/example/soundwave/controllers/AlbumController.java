package org.example.soundwave.controllers;

import lombok.AllArgsConstructor;
import org.example.soundwave.dto.AlbumDTO;
import org.example.soundwave.services.AlbumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping({"/user/albums", "/admin/albums"})
    public ResponseEntity<?> listAlbums(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AlbumDTO> albums = albumService.listAlbums(PageRequest.of(page, size));
        return ResponseEntity.ok(albums);
    }

    @GetMapping({"/user/albums/search", "/admin/albums/search"})
    public ResponseEntity<?> searchAlbumsByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<AlbumDTO> albums = albumService.searchAlbumsByTitle(
                title, PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy)));
        return ResponseEntity.ok(albums);
    }

    @GetMapping({"/user/albums/artist", "/admin/albums/artist"})
    public ResponseEntity<?> searchAlbumsByArtist(
            @RequestParam String artist,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AlbumDTO> albums = albumService.searchAlbumsByArtist(artist, PageRequest.of(page, size));
        return ResponseEntity.ok(albums);
    }

    @GetMapping({"/user/albums/filter", "/admin/albums/filter"})
    public ResponseEntity<?> filterAlbumsByYear(
            @RequestParam int year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "year") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<AlbumDTO> albums = albumService.filterAlbumsByYear(
                year, PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy)));
        return ResponseEntity.ok(albums);
    }

    @PostMapping("/admin/albums")
    public ResponseEntity<?> addAlbum(@RequestBody @Valid AlbumDTO albumDTO) {
        AlbumDTO createdAlbum = albumService.addAlbum(albumDTO);
        return ResponseEntity.ok(createdAlbum);
    }

    @PutMapping("/admin/albums/{id}")
    public ResponseEntity<?> updateAlbum(@PathVariable String id, @RequestBody @Valid AlbumDTO albumDTO) {
        AlbumDTO updatedAlbum = albumService.updateAlbum(id, albumDTO);
        return ResponseEntity.ok(updatedAlbum);
    }

    @DeleteMapping("/admin/albums/{id}")
    public ResponseEntity<?> deleteAlbum(@PathVariable String id) {
        albumService.deleteAlbum(id);
        return ResponseEntity.ok("Album deleted successfully");
    }
}
