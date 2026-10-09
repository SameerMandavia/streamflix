import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home.component';
import { SearchComponent } from './pages/search.component';
import { MovieDetailsComponent } from './pages/movie-details.component';
import { LoginComponent } from './pages/login.component';
import { RegisterComponent } from './pages/register.component';
import { ProfilesComponent } from './pages/profiles.component';
import { authGuard } from './auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent, title: 'StreamFlix — Home' },
  { path: 'search', component: SearchComponent, title: 'StreamFlix — Search' },
  { path: 'movies/:id', component: MovieDetailsComponent, title: 'StreamFlix — Movie' },
  { path: 'login', component: LoginComponent, title: 'StreamFlix — Login' },
  { path: 'register', component: RegisterComponent, title: 'StreamFlix — Register' },
  { path: 'profiles', component: ProfilesComponent, canActivate: [authGuard], title: 'StreamFlix — Profiles' },
  { path: '**', redirectTo: '' },
];
