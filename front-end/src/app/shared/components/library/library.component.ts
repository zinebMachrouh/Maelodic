import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {PlayerComponent} from '../../../modules/player/components/player/player.component';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {TrackListComponent} from '../../../modules/track/components/track-list/track-list.component';
import {Track} from "../../../modules/track/models/track.model";
import {AuthService} from "../../../modules/auth/services/auth.service";
import {TrackPlayService} from "../../../modules/track/services/track-play.service";

@Component({
  selector: 'app-library',
  imports: [
    PlayerComponent,
    RouterLinkActive,
    RouterLink,
    RouterOutlet
  ],
  templateUrl: './library.component.html',
  standalone: true,
  styleUrl: './library.component.scss'
})
export class LibraryComponent implements OnInit {
  selectedTrack: Track | null = null;
  username: string | null = null;
  @Output() playTrack = new EventEmitter<Track>();

  constructor(private authService: AuthService,private trackPlayService: TrackPlayService, private router: Router) {}

  ngOnInit(): void {
    const username1:string | null = this.authService.getUsername();
    // @ts-ignore
    const firstLetter = username1.charAt(0);

    const firstLetterCap = firstLetter.toUpperCase();

    // @ts-ignore
    const remainingLetters = username1.slice(1);

    this.username = firstLetterCap + remainingLetters;

    this.trackPlayService.setUsername(this.username);  // Set the username
    this.trackPlayService.playTrack$.subscribe((track: Track) => {
      this.selectedTrack = track;
    });
  }

  onPlayTrack(track: Track): void {
    this.selectedTrack = track;
    this.playTrack.emit(track);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}
