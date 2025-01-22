package org.example.soundwave.controllers;

import lombok.AllArgsConstructor;
import org.example.soundwave.dto.SongDTO;
import org.example.soundwave.services.SongService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class SongController {

    private final SongService songService;

    @GetMapping({"/user/songs", "/admin/songs"})
    public ResponseEntity<?> listSongs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<SongDTO> songs = songService.listSongs(PageRequest.of(page, size));
        return ResponseEntity.ok(songs);
    }

    @GetMapping({"/user/songs/search", "/admin/songs/search"})
    public ResponseEntity<?> searchSongsByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<SongDTO> songs = songService.searchSongsByTitle(
                title, PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy)));
        return ResponseEntity.ok(songs);
    }

    @GetMapping({"/user/songs/album", "/admin/songs/album"})
    public ResponseEntity<?> listSongsByAlbum(
            @RequestParam String albumId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "trackNumber") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<SongDTO> songs = songService.listSongsByAlbum(
                albumId, PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy)));
        return ResponseEntity.ok(songs);
    }

    @PostMapping("/admin/songs")
    public ResponseEntity<?> addSong(@RequestBody @Valid SongDTO songDTO) {
        SongDTO createdSong = songService.addSong(songDTO);
        return ResponseEntity.ok(createdSong);
    }

    @PutMapping("/admin/songs/{id}")
    public ResponseEntity<?> updateSong(@PathVariable String id, @RequestBody @Valid SongDTO songDTO) {
        SongDTO updatedSong = songService.updateSong(id, songDTO);
        return ResponseEntity.ok(updatedSong);
    }

    @DeleteMapping("/admin/songs/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable String id) {
        songService.deleteSong(id);
        return ResponseEntity.ok("Song deleted successfully");
    }
}
