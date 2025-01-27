import { Injectable } from '@angular/core';
import {BehaviorSubject, Subject} from "rxjs";
import {Track} from "../models/track.model";

@Injectable({
  providedIn: 'root'
})
export class TrackPlayService {

  private playTrackSubject = new Subject<Track>();
  playTrack$ = this.playTrackSubject.asObservable();

  playTrack(track: Track) {
    this.playTrackSubject.next(track);
  }

  private usernameSubject = new BehaviorSubject<string>('GUest');
  username$ = this.usernameSubject.asObservable();

  setUsername(username: string) {
    this.usernameSubject.next(username);
  }
}
