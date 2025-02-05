import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {RouterLink} from '@angular/router';
import {MusicCategory} from '../../models/enums/MusicCategory';
import {CommonModule} from '@angular/common';
import {TrackCardComponent} from '../track-card/track-card.component';
import {TrackFormComponent} from '../track-form/track-form.component';
import {Track} from '../../models/track.model';
import {TrackService} from "../../services/track.service";
import {debounceTime, distinctUntilChanged, from} from "rxjs";
import {FormControl, ReactiveFormsModule} from "@angular/forms";
import {TrackPlayService} from "../../services/track-play.service";

@Component({
  selector: 'app-track-list',
  imports: [
    RouterLink,
    CommonModule,
    TrackCardComponent,
    TrackFormComponent,
    ReactiveFormsModule
  ],
  templateUrl: './track-list.component.html',
  standalone: true,
  styleUrl: './track-list.component.scss'
})
export class TrackListComponent implements OnInit {
  @Output() playTrack = new EventEmitter<Track>();

  tracks: Track[] = [];
  originalTracks: Track[] = [];
  tracksCount: number = 0;
  showModal = false;
  musicCategories: MusicCategory[] = [];
  selectedTrack: Track | null = null;
  searchControl = new FormControl('');
  activeCategory: string = 'All';
  @Input() username!: string | null;
  @Input() isAdmin!: boolean;
  @Input() onPlayTrack!: (track: Track) => void;


  constructor(private trackService: TrackService, private trackPlayService: TrackPlayService) {}

  ngOnInit(): void {
    this.musicCategories = Object.values(MusicCategory) as MusicCategory[];
    this.getAllTracks();

    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(value => {
      this.searchTracks(value || '');
    });

    this.trackPlayService.username$.subscribe((username) => {
      this.username = username;
    });
  }

  getAllTracks(): void {
    this.activeCategory = 'All';
    from(this.trackService.getTracks()).subscribe({
      next: (response) => {
        this.tracks = response.content;
        this.originalTracks = [...this.tracks];
        this.tracksCount = this.tracks.length;

        console.log('Tracks:', this.tracks);
      },
      error: (error: any) => {
        console.error('Error:', error);
      },
    });
  }

  editTrack(track : Track): void {
    console.log('Edit track');
    this.selectedTrack = track;
    this.showModal = true;
  }

  onTrackPlay(track: Track): void {
    this.trackPlayService.playTrack(track);
  }

  openModal(): void {
    this.selectedTrack = null;
    this.showModal = true;
  }

  onCloseModal(): void {
    this.showModal = false;
  }

  deleteTrack(id: string): void {
    confirm('Are you sure you want to delete this track?') && from(this.trackService.deleteTrack(id)).subscribe({
      next: () => {
        this.getAllTracks();
      },
      error: (error: any) => {
        console.error('Error:', error);
      },
    });

    this.getAllTracks();
  }

  searchTracks(searchTerm: string): void {
    if (!searchTerm.trim()) {
      this.tracks = [...this.originalTracks];
      return;
    }

    const term = searchTerm.toLowerCase();
    this.tracks = this.originalTracks.filter(track =>
      track.title.toLowerCase().includes(term) ||
      // @ts-ignore
      track.description.toLowerCase().includes(term)
    );
  }

  filterByCategory(category: MusicCategory): void {
    this.activeCategory = category;
    this.tracks = this.originalTracks.filter(track => track.category === category);
  }

  onSaveTrack(event: { formData: FormData, audioFile: File, imageFile: File }): void {
    const { formData, audioFile, imageFile } = event;
    if (this.selectedTrack) {
      from(this.trackService.updateTrack(this.selectedTrack.id, formData)).subscribe({
        next: () => {
          this.getAllTracks();
          this.showModal = false;
        },
        error: (error: any) => {
          console.error('Error:', error);
        },
      });
    } else {
      from(this.trackService.addTrack(formData, audioFile, imageFile)).subscribe({
        next: () => {
          this.getAllTracks();
          this.showModal = false;
        },
        error: (error: any) => {
          console.error('Error:', error);
        },
      });
    }
  }

  protected readonly File = File;
}
