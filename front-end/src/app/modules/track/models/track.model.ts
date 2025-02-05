import {MusicCategory} from './enums/MusicCategory';
import {Album} from "../../album/models/album.model";

export interface Track {
  id: string;
  title: string;
  trackNumber: number;
  description?: string;
  addedDate: string;
  duration: number;
  category: MusicCategory;
  imageUrl?: string;
  audioUrl: string;
  albumId : string
}
