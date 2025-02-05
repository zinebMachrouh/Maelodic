import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MusicCategory } from '../../models/enums/MusicCategory';
import {Track} from '../../models/track.model';
import {AlbumService} from "../../../album/services/album.service";
import {Album} from "../../../album/models/album.model";

@Component({
  selector: 'app-track-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './track-form.component.html',
  standalone: true,
  styleUrls: ['./track-form.component.scss']
})
export class TrackFormComponent implements OnChanges {
  @Input() categories: MusicCategory[] = Object.values(MusicCategory) as MusicCategory[];
  @Input() isVisible = false;
  @Input() track: Track | null = null;
  @Output() close = new EventEmitter();
  @Output() save = new EventEmitter<any>();

  public albums : Album[] | [] = [];

  audioFile: File | null = null;
  imageFile: File | null = null;

  duration: string = '';


  trackForm: FormGroup;

  constructor(private fb: FormBuilder, private albumService : AlbumService) {
    this.trackForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      trackNumber: ['', [Validators.required, Validators.min(1)]],
      description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      category: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      albumId: ['', Validators.required],
      audioUrl: [null],
      imageUrl: [null],
    });

    this.albumService.getAllAlbums().subscribe({
      next: (response:any) => {
        this.albums = response;
      },
      error: (error) => {
        console.error(error);
      }
    });
    }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['track'] && this.track) {
      this.trackForm.patchValue(this.track);
    }
  }

  closeModal(): void {
    this.close.emit();
  }

  onAudioFileSelected(event: any) {
    const input = event.target as HTMLInputElement;
    if (input?.files?.length) {
      this.audioFile = input.files[0];

      const audio = new Audio(URL.createObjectURL(this.audioFile));
      audio.addEventListener('loadedmetadata', () => {
        const durationInSeconds = Math.floor(audio.duration);
        this.duration = durationInSeconds.toString();
        console.log('Audio Duration:', durationInSeconds);
      });
    }  }

  onImageFileSelected(event: any) {
    this.imageFile = event.target.files[0];
  }

  onSubmit(): void {
    const formData = new FormData();
    Object.keys(this.trackForm.controls).forEach(key => {
      formData.append(key, this.trackForm.get(key)?.value);
    });


    formData.append('duration', this.duration);

    this.save.emit({formData, audioFile: this.audioFile, imageFile: this.imageFile});
  }

  /**
   * Custom Validator for Image File
   * - Accepted formats: PNG, JPEG
   */
  private imageValidator(control: any): { [key: string]: any } | null {
    const file = control.value as File;
    if (!file) return null;

    const validExtensions = ['image/png', 'image/jpeg'];
    if (!validExtensions.includes(file.type)) {
      return { invalidImageFormat: true };
    }
    return null;
  }

  /**
   * Custom Validator for Audio File
   * - Max size: 15MB
   * - Accepted formats: MP3, WAV, OGG
   */
  private audioValidator(control: any): { [key: string]: any } | null {
    const file = control.value as File;
    if (!file) return null;

    const validExtensions = ['audio/mpeg', 'audio/wav', 'audio/ogg'];
    const maxSize = 15 * 1024 * 1024; // 15MB

    if (!validExtensions.includes(file.type)) {
      return { invalidAudioFormat: true };
    }

    if (file.size > maxSize) {
      return { invalidAudioSize: true };
    }

    return null;
  }
}
