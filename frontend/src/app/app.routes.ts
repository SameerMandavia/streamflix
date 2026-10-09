import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home.component';
import { SearchComponent } from './pages/search.component';
import { MovieDetailsComponent } from './pages/movie-details.component';
import { LoginComponent } from './pages/login.component';
import { RegisterComponent } from './pages/register.component';
import { ProfilesComponent } from './pages/profiles.component';
import { MyListComponent } from './pages/my-list.component';
import { HistoryComponent } from './pages/history.component';
import { SeriesDetailsComponent } from './pages/series-details.component';
import { VideoPlayerComponent } from './pages/video-player.component';
import { SubscriptionsComponent } from './pages/subscriptions.component';
import { authGuard } from './auth.guard';
import { adminGuard } from './admin.guard';
import { AdminDashboardComponent } from './pages/admin-dashboard.component';

export const routes: Routes = [
  { path: '', component: HomeComponent, title: 'StreamFlix — Home' },
  { path: 'search', component: SearchComponent, title: 'StreamFlix — Search' },
  { path: 'movies/:id', component: MovieDetailsComponent, title: 'StreamFlix — Movie' },
  { path: 'series/:id', component: SeriesDetailsComponent, title: 'StreamFlix — Series' },
  { path: 'watch/:movieId', component: VideoPlayerComponent, canActivate: [authGuard], title: 'StreamFlix — Player' },
  { path: 'subscriptions', component: SubscriptionsComponent, canActivate: [authGuard], title: 'StreamFlix — Plans' },
  { path: 'admin', component: AdminDashboardComponent, canActivate: [authGuard, adminGuard], title: 'StreamFlix — Admin' },
  { path: 'login', component: LoginComponent, title: 'StreamFlix — Login' },
  { path: 'register', component: RegisterComponent, title: 'StreamFlix — Register' },
  { path: 'profiles', component: ProfilesComponent, canActivate: [authGuard], title: 'StreamFlix — Profiles' },
  { path: 'my-list', component: MyListComponent, canActivate: [authGuard], title: 'StreamFlix — My List' },
  { path: 'history', component: HistoryComponent, canActivate: [authGuard], title: 'StreamFlix — History' },
  { path: '**', redirectTo: '' },
];
