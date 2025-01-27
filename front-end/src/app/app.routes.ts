import { Routes } from '@angular/router';
import {DashboardComponent} from "./shared/components/dashboard/dashboard.component";
import {LibraryComponent} from "./shared/components/library/library.component";
import {LoginComponent} from "./modules/auth/components/login/login.component";
import {RegisterComponent} from "./modules/auth/components/register/register.component";
import {AuthGuard} from "./modules/auth/guards/auth.guard";
import {userResolver} from "./modules/auth/resolvers/user.resolver";
import {AlbumListComponent} from "./modules/album/components/album-list/album-list.component";
import {TrackListComponent} from "./modules/track/components/track-list/track-list.component";

export const routes: Routes = [
  {path: '' , redirectTo: 'home', pathMatch: 'full'},
  {path: 'home', component : DashboardComponent},
  {
    path: 'library',
    component:LibraryComponent,
    canActivate: [AuthGuard],
    resolve: { user: userResolver },
    children: [
      {
        path: '',
        redirectTo: 'tracks',
        pathMatch: 'full',
      },
      {
        path: 'tracks',
        component: TrackListComponent
      },
      {
        path: 'albums',
        component: AlbumListComponent
      }
    ]
  },
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
];
