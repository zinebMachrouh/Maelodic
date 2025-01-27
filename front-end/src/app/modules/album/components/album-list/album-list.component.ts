import {Component, Input, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {TrackPlayService} from "../../../track/services/track-play.service";
import {NgForOf, NgIf} from "@angular/common";
import {Album} from "../../models/album.model";
import {AlbumCardComponent} from "../album-card/album-card.component";
import {AlbumService} from "../../services/album.service";
import {AuthService} from "../../../auth/services/auth.service";
import {debounceTime, distinctUntilChanged, from} from "rxjs";
import {AlbumFormComponent} from "../album-form/album-form.component";
import {switchMap} from "rxjs/operators";
import {FormControl, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-album-list',
  standalone: true,
  imports: [
    RouterLink,
    NgForOf,
    NgIf,
    AlbumCardComponent,
    AlbumFormComponent,
    ReactiveFormsModule
  ],
  templateUrl: './album-list.component.html',
  styleUrl: './album-list.component.scss'
})
export class AlbumListComponent implements OnInit {
  username: string | null = "Guest";
  albumsCount: number | null = 0;
  albums: Album[] = [];
  isLoading: boolean = true;
  error: string | null = null;
  showModal = false;
  selectedAlbum: Album | null = null;
  isAdmin: boolean = false;

  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;

  searchQuery: string = '';
  searchControl: FormControl = new FormControl('');

  constructor(
    private authService: AuthService,
    private trackPlayService: TrackPlayService,
    private albumService: AlbumService
  ) {
    this.isAdmin = this.authService.isAdmin();
  }

  ngOnInit(): void {
    this.trackPlayService.username$.subscribe((username) => {
      this.username = username;
    });

    this.isAdmin = this.authService.isAdmin();
    this.fetchAlbums();

    this.searchControl.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((query) => {
          this.searchQuery = query || ''; // Update the search query
          this.currentPage = 0; // Reset to the first page for a new search
          return this.performSearch();
        })
      )
      .subscribe({
        next: (response: any) => {
          this.albums = response.content; // Update the albums array with results
          this.albumsCount = response.totalElements;
          this.totalPages = Math.ceil(response.totalElements / this.pageSize);
          this.isLoading = false;
        },
        error: (err: any) => {
          console.error('Error during search:', err);
          this.error = 'Failed to search albums. Please try again.';
          this.isLoading = false;
        },
      });
  }

  private fetchAlbums(): void {
    this.isLoading = true;
    this.albumService.getAlbums(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.albums = response.content;
        this.albumsCount = response.totalElements;
        this.totalPages = Math.ceil(response.totalElements / this.pageSize);
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching albums:', err);
        this.error = 'Failed to load albums. Please try again later.';
        this.isLoading = false;
      }
    });
  }

  private performSearch() {
    if (!this.searchQuery) {
      return this.albumService.getAlbums(this.currentPage, this.pageSize);
    } else if (!isNaN(Number(this.searchQuery))) {
      console.log('Searching by year:', this.searchQuery);
      return this.albumService.filterAlbumsByYear(Number(this.searchQuery), this.currentPage, this.pageSize, 'year', 'asc');
    } else if (this.isArtistQuery(this.searchQuery)) {
      return this.albumService.searchAlbumsByArtist(this.searchQuery, this.currentPage, this.pageSize);
    } else {
      return this.albumService.searchAlbumsByTitle(this.searchQuery, this.currentPage, this.pageSize);
    }
  }

  public prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.refreshData();
    }
  }

  public nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.refreshData();
    }
  }

  private refreshData(): void {
    this.isLoading = true;
    this.performSearch().subscribe({
      next: (response) => {
        this.albums = response.content;
        this.albumsCount = response.totalElements;
        this.totalPages = Math.ceil(response.totalElements / this.pageSize);
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error refreshing data:', err);
        this.error = 'Failed to load data. Please try again.';
        this.isLoading = false;
      }
    });
  }

  private isArtistQuery(query: string): boolean {
    return query.split(' ').length > 1;
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
    if (confirm('Are you sure you want to delete this album?')) {
      from(this.albumService.deleteAlbum(id)).subscribe({
        next: () => {
          this.fetchAlbums();
        },
        error: (error: any) => {
          console.error('Error:', error);
        },
      });
    }
    this.fetchAlbums();
  }
}
