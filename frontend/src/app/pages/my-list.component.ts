import { Component, inject } from '@angular/core';
import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { catchError, of } from 'rxjs';
import { AuthService } from '../auth.service';
import { Movie } from '../movie.service';
import { UserFeatureService } from '../user-feature.service';
import { MovieCardComponent } from '../components/movie-card.component';
@Component({ selector: 'app-my-list', standalone: true, imports: [AsyncPipe, NgFor, NgIf, MovieCardComponent], template: `<main class="content-shell page-space"><p class="eyebrow">YOUR COLLECTION</p><h1 class="page-title">My List</h1><ng-container *ngIf="movies$ | async as movies"><div class="movie-grid"><app-movie-card *ngFor="let movie of movies" [movie]="movie" /></div><p class="empty-state" *ngIf="!movies.length">Your list is empty. Add movies from their detail pages.</p></ng-container></main>` })
export class MyListComponent { private readonly features = inject(UserFeatureService); readonly movies$ = this.features.getWatchlist().pipe(catchError(() => of([] as Movie[]))); }
