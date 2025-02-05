package org.example.soundwave.services;

import org.example.soundwave.dto.SongDTO;
import org.example.soundwave.dto.SongRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface SongService {

    Page<SongDTO> listSongs(Pageable pageable);

    Page<SongDTO> searchSongsByTitle(String title, Pageable pageable);

    Page<SongDTO> listSongsByAlbum(String albumId, Pageable pageable);

    SongDTO getSong(String id);

    SongDTO addSong(SongRequestDTO songDTO, MultipartFile audioFile, MultipartFile imageFile);

    SongDTO updateSong(String id, SongDTO songDTO);

    void deleteSong(String id);
}
