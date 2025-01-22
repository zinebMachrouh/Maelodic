package org.example.soundwave.services.impl;

import lombok.AllArgsConstructor;
import org.example.soundwave.dto.SongDTO;
import org.example.soundwave.entities.Album;
import org.example.soundwave.entities.Song;
import org.example.soundwave.repositories.AlbumRepository;
import org.example.soundwave.repositories.SongRepository;
import org.example.soundwave.services.SongService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    @Override
    public Page<SongDTO> listSongs(Pageable pageable) {
        return songRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Override
    public Page<SongDTO> searchSongsByTitle(String title, Pageable pageable) {
        return songRepository.findByTitleContainingIgnoreCase(title, pageable).map(this::mapToDTO);
    }

    @Override
    public Page<SongDTO> listSongsByAlbum(String albumId, Pageable pageable) {
        return songRepository.findByAlbum_Id(albumId, pageable).map(this::mapToDTO);
    }

    @Override
    public SongDTO addSong(SongDTO songDTO) {
        Optional<Album> album = albumRepository.findById(songDTO.getAlbumId());
        if (album.isEmpty()) {
            throw new IllegalArgumentException("Invalid album ID");
        }

        Song song = mapToEntity(songDTO);
        song.setAlbum(album.get());
        return mapToDTO(songRepository.save(song));
    }

    @Override
    public SongDTO updateSong(String id, SongDTO songDTO) {
        Optional<Song> existingSong = songRepository.findById(id);
        if (existingSong.isEmpty()) {
            throw new IllegalArgumentException("Song not found");
        }

        Optional<Album> album = albumRepository.findById(songDTO.getAlbumId());
        if (album.isEmpty()) {
            throw new IllegalArgumentException("Invalid album ID");
        }

        Song updatedSong = existingSong.get();
        updatedSong.setTitle(songDTO.getTitle());
        updatedSong.setDuration(songDTO.getDuration());
        updatedSong.setTrackNumber(songDTO.getTrackNumber());
        updatedSong.setAlbum(album.get());

        return mapToDTO(songRepository.save(updatedSong));
    }

    @Override
    public void deleteSong(String id) {
        if (!songRepository.existsById(id)) {
            throw new IllegalArgumentException("Song not found");
        }
        songRepository.deleteById(id);
    }

    private SongDTO mapToDTO(Song song) {
        return SongDTO.builder()
                .id(song.getId())
                .title(song.getTitle())
                .duration(song.getDuration())
                .trackNumber(song.getTrackNumber())
                .albumId(song.getAlbum().getId())
                .build();
    }

    private Song mapToEntity(SongDTO songDTO) {
        Song song = new Song();
        song.setId(songDTO.getId());
        song.setTitle(songDTO.getTitle());
        song.setDuration(songDTO.getDuration());
        song.setTrackNumber(songDTO.getTrackNumber());
        return song;
    }
}
