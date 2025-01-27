import { Component } from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import {AuthService} from "../../../modules/auth/services/auth.service";

@Component({
  selector: 'app-dashboard',
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './dashboard.component.html',
  standalone: true,
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {
  constructor(private authService : AuthService, private router : Router) {}

  login(): void {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/library']);
    }else {
      this.router.navigate(['/login']);
    }
  }
}
