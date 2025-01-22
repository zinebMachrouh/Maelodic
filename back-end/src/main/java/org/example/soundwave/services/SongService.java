package org.example.soundwave.services;

import org.example.soundwave.dto.SongDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SongService {

    Page<SongDTO> listSongs(Pageable pageable);

    Page<SongDTO> searchSongsByTitle(String title, Pageable pageable);

    Page<SongDTO> listSongsByAlbum(String albumId, Pageable pageable);

    SongDTO addSong(SongDTO songDTO);

    SongDTO updateSong(String id, SongDTO songDTO);

    void deleteSong(String id);
}
