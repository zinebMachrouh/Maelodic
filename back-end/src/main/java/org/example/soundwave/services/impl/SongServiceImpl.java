package org.example.soundwave.services.impl;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import org.example.soundwave.dto.SongDTO;
import org.example.soundwave.dto.SongRequestDTO;
import org.example.soundwave.entities.Album;
import org.example.soundwave.entities.Song;
import org.example.soundwave.repositories.AlbumRepository;
import org.example.soundwave.repositories.SongRepository;
import org.example.soundwave.services.FileService;
import org.example.soundwave.services.SongService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@Service
@AllArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final FileService fileService;

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
    public SongDTO getSong(String id) {
        return songRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));
    }

    @Override
    public SongDTO addSong(SongRequestDTO songDTO,MultipartFile audioFile, MultipartFile imageFile) {
        Optional<Album> album = albumRepository.findById(songDTO.getAlbumId());

        if (album.isEmpty()) {
            throw new IllegalArgumentException("Invalid album ID");
        }

        Song song = mapToEntity(songDTO);
        song.setAlbum(album.get());
        song.setAddedDate(LocalDateTime.now());

        if (audioFile != null && !audioFile.isEmpty()) {
            String audioUrl = fileService.saveFile(audioFile);
            song.setAudioUrl(audioUrl);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileService.saveFile(imageFile);
            song.setImageUrl(imageUrl);
        }

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
        updatedSong.setTrackNumber(songDTO.getTrackNumber());
        updatedSong.setDescription(songDTO.getDescription());
        updatedSong.setCategory(songDTO.getCategory());

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
                .trackNumber(song.getTrackNumber())
                .addedDate(song.getAddedDate())
                .description(song.getDescription())
                .category(song.getCategory())
                .duration(song.getDuration())
                .albumId(song.getAlbum().getId())
                .build();
    }

    private Song mapToEntity(SongRequestDTO songDTO) {
        Song song = new Song();
        song.setId(songDTO.getId());
        song.setTitle(songDTO.getTitle());
        song.setTrackNumber(songDTO.getTrackNumber());
        song.setDescription(songDTO.getDescription());
        song.setCategory(songDTO.getCategory());
        return song;
    }
}
