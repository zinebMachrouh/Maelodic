import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Album} from "../../models/album.model";

@Component({
  selector: 'app-album-form',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    NgClass
  ],
  templateUrl: './album-form.component.html',
  styleUrl: './album-form.component.scss'
})
export class AlbumFormComponent implements OnChanges{
  @Input() isVisible = false;
  @Input() album: Album | null = null;
  @Output() close = new EventEmitter();
  @Output() save = new EventEmitter();

  albumForm: FormGroup;
  currentYear: number = new Date().getFullYear();

  constructor(private fb: FormBuilder) {
    this.albumForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      artist: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      year: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(4), Validators.min(1900), Validators.max(this.currentYear)]],
    });

    console.log(this.currentYear);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['album'] && this.album) {
      this.albumForm.patchValue(this.album);
    }
  }

  closeModal(): void {
    this.close.emit();
  }

  onSubmit(): void {
    if (this.albumForm.valid) {
      const { title, artist, year } = this.albumForm.value;
      const albumData = { title, artist, year };
      this.save.emit({ albumData });
    }
  }
}
