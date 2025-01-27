import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Album} from "../../models/album.model";
import {AlbumService} from "../../services/album.service";

@Component({
  selector: 'app-album-card',
  standalone: true,
  imports: [],
  templateUrl: './album-card.component.html',
  styleUrl: './album-card.component.scss'
})
export class AlbumCardComponent {
  @Input() album!: Album;
  @Output() delete = new EventEmitter();
  @Output() edit = new EventEmitter<unknown>();
  @Input() isAdmin!: boolean;

  constructor(private albumService: AlbumService) {
  }

  onDelete(): void {
    this.delete.emit(this.album.id);
  }

  onEdit(): void {
    this.edit.emit(this.album);
  }
}
