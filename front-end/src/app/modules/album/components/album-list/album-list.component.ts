import {Component, Input, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {TrackPlayService} from "../../../track/services/track-play.service";
import {NgForOf, NgIf} from "@angular/common";
import {Album} from "../../models/album.model";
import {AlbumCardComponent} from "../album-card/album-card.component";
import {AlbumService} from "../../services/album.service";
import {AuthService} from "../../../auth/services/auth.service";
import {from} from "rxjs";
import {TrackFormComponent} from "../../../track/components/track-form/track-form.component";
import {AlbumFormComponent} from "../album-form/album-form.component";

@Component({
  selector: 'app-album-list',
  standalone: true,
  imports: [
    RouterLink,
    NgForOf,
    NgIf,
    AlbumCardComponent,
    AlbumFormComponent
  ],
  templateUrl: './album-list.component.html',
  styleUrl: './album-list.component.scss'
})
export class AlbumListComponent implements OnInit{
  username: string | null = "Guest";
  albumsCount: number | null = 0;
  albums: Album[] = [];
  isLoading: boolean = true;
  error: string | null = null;
  showModal = false;
  selectedAlbum: Album | null = null;
  isAdmin: boolean = false;

  constructor(
    private authService: AuthService,
    private trackPlayService: TrackPlayService,
    private albumService: AlbumService
  ) {}

  ngOnInit(): void {
    this.trackPlayService.username$.subscribe((username) => {
      this.username = username;
    });

    this.isAdmin = this.authService.isAdmin();

    this.fetchAlbums();
  }

  private fetchAlbums(): void {
    this.isLoading = true;
    this.albumService.getAlbums(0, 10).subscribe({
      next: (response) => {
        this.albums = response.content;
        this.albumsCount = response.totalElements;
        this.isLoading = false;
        console.log('Error fetching albums:', response.content);
      },
      error: (err) => {
        console.error('Error fetching albums:', err);
        this.error = 'Failed to load albums. Please try again later.';
        this.isLoading = false;
      }
    });
  }

  public prevPage(): void {
    console.log('Previous page');
  }

  public nextPage(): void {
    console.log('Next page');
  }

  onCloseModal(): void {
    this.showModal = false;
  }

  onSaveAlbum(albumData: Album) {
    if (this.selectedAlbum) {
      from(this.albumService.updateAlbum(this.selectedAlbum.id, albumData)).subscribe({
        next: () => {
          this.fetchAlbums();
          this.showModal = false;
        },
        error: (error: any) => {
          console.error('Error:', error);
        },
      });
    } else {
      from(this.albumService.addAlbum(albumData)).subscribe({
        next: () => {
          this.fetchAlbums();
          this.showModal = false;
        },
        error: (error: any) => {
          console.error('Error:', error);
        },
      });
    }
  }

  openModal() {
    this.selectedAlbum = null;
    this.showModal = true;
  }

  editAlbum(album: Album) {
    this.selectedAlbum = album;
    this.showModal = true;
  }

  deleteAlbum(id: string) {
    confirm('Are you sure you want to delete this album?') && from(this.albumService.deleteAlbum(id)).subscribe({
      next: () => {
        this.fetchAlbums();
      },
      error: (error: any) => {
        console.error('Error:', error);
      },
    });

    this.fetchAlbums();
  }
}
