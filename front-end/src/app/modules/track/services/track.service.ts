import { Injectable } from '@angular/core';
import {Observable, from, firstValueFrom} from 'rxjs';
import { Track } from '../models/track.model';
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class TrackService {
  private baseUrl = 'http://localhost:8085/api/v1';


  constructor(private http :HttpClient) {}

   getTracks(page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get(`${this.baseUrl}/user/songs`, { params });
  }

   getTrack(id: string): Observable<any>{
    return this.http.get(`${this.baseUrl}/user/songs/${id}`);
  }

  async addTrack(trackData: any, audioFile : File, imageFile : File): Promise<Observable<any>> {
    const formData = new FormData();

    formData.append('id', crypto.randomUUID());
    formData.append('title', trackData.get('title'));
    formData.append('trackNumber', trackData.get('trackNumber') || '0');
    formData.append('description', trackData.get('description'));
    formData.append('category', trackData.get('category'));
    formData.append('duration', trackData.get('duration'));
    formData.append('albumId', trackData.get('albumId'));

    if (trackData.get('audioUrl') instanceof File) {
      console.log('audioUrl is a file');
      formData.append('audioUrl', trackData.get('audioUrl'));
    }
    if (trackData.get('imageUrl') instanceof File) {
      console.log('imageUrl is a file');
      formData.append('imageUrl', trackData.get('imageUrl'));
    }

    return this.http.post(`${this.baseUrl}/admin/songs`, formData);
  }

  updateTrack(id: string, trackData:any): Observable<any> {
    const formData = new FormData();

    formData.append('id', crypto.randomUUID());
    formData.append('title', trackData.get('title'));
    formData.append('trackNumber', trackData.get('trackNumber') || '0');
    formData.append('description', trackData.get('description'));
    formData.append('category', trackData.get('category'));
    formData.append('duration', trackData.get('duration'));
    formData.append('albumId', trackData.get('albumId'))

    if (trackData.get('audioUrl') instanceof File) {
      console.log('audioUrl is a file');
      formData.append('audioUrl', trackData.get('audioUrl'));
    }
    if (trackData.get('imageUrl') instanceof File) {
      console.log('imageUrl is a file');
      formData.append('imageUrl', trackData.get('imageUrl'));
    }

    return this.http.put(`${this.baseUrl}/admin/songs/${id}`, formData);
  }

  deleteTrack(id: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/admin/songs/${id}`);
  }

  private validateAudioFile(file: File): Observable<void> {
    return from(new Promise<void>((resolve, reject) => {
      if (file.size > 15 * 1024 * 1024) { // 15MB limit
        reject(new Error('File size exceeds 15MB limit'));
      }

      const validTypes = ['audio/mp3', 'audio/wav', 'audio/ogg'];
      if (!validTypes.includes(file.type)) {
        reject(new Error('Invalid audio file format. Supported formats: MP3, WAV, OGG'));
      }

      resolve();
    }));
  }

  private async validateImageFile(file: File): Promise<void> {
    const validTypes = ['image/jpeg', 'image/png'];
    if (!validTypes.includes(file.type)) {
      throw new Error('Invalid image format. Supported formats: JPEG, PNG');
    }
  }

}
