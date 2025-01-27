import { Injectable } from '@angular/core';
import {Album} from "../models/album.model";
import {Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AlbumService {

  private baseUrl = 'http://localhost:8085/api/v1';

  constructor(private http: HttpClient) {}

  getAlbums(page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get(`${this.baseUrl}/user/albums`, { params });
  }

  searchAlbumsByTitle(
    title: string,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'title',
    direction: string = 'asc'
  ): Observable<any> {
    const params = new HttpParams()
      .set('title', title)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('direction', direction);
    return this.http.get(`${this.baseUrl}/user/albums/search`, { params });
  }

  searchAlbumsByArtist(
    artist: string,
    page: number = 0,
    size: number = 10
  ): Observable<any> {
    const params = new HttpParams()
      .set('artist', artist)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get(`${this.baseUrl}/user/albums/artist`, { params });
  }

  filterAlbumsByYear(
    year: number,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'year',
    direction: string = 'asc'
  ): Observable<any> {
    const params = new HttpParams()
      .set('year', year.toString())
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('direction', direction);
    return this.http.get(`${this.baseUrl}/user/albums/filter`, { params });
  }

  addAlbum(album: any): Observable<any> {
    return this.http.post(`http://localhost:8085/api/v1/admin/albums`, album, {
      headers: { 'Content-Type': 'application/json' }
    });
  }

  updateAlbum(id: string, album: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/admin/albums/${id}`, album);
  }

  deleteAlbum(id: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/admin/albums/${id}`);
  }
}
