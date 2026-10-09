import { Component, inject } from '@angular/core';
import { AsyncPipe, NgIf } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { catchError, of, switchMap } from 'rxjs';
import { MovieService } from '../movie.service';
import { AuthService } from '../auth.service';
import { UserFeatureService } from '../user-feature.service';

@Component({ selector: 'app-movie-details', standalone: true, imports: [AsyncPipe, NgIf, RouterLink], template: `
  <main class="details-page" *ngIf="movie$ | async as movie"><div class="details-backdrop" [style.background-image]="'url(' + movie.posterUrl + ')'" ></div><div class="details-content content-shell"><a routerLink="/" class="back-link">← Back to catalogue</a><div class="details-copy"><p class="eyebrow">{{ movie.genre }} · {{ movie.releaseYear }}</p><h1 class="page-title">{{ movie.title }}</h1><p class="details-meta">{{ movie.durationMinutes }} minutes · Feature film</p><p class="details-description">{{ movie.description }}</p><div class="details-actions"><button class="button button-primary" (click)="start(movie.id)">▶ Start watching</button><button class="button button-secondary" *ngIf="auth.isLoggedIn()" (click)="save(movie.id)">{{ saved ? '✓ In My List' : '+ My List' }}</button></div><p class="action-message" *ngIf="message">{{ message }}</p></div></div></main>
  <main class="content-shell page-space empty-state" *ngIf="(movie$ | async) === null">Movie not found.</main>
` })
export class MovieDetailsComponent {
  private readonly route = inject(ActivatedRoute); private readonly router = inject(Router); private readonly service = inject(MovieService); readonly auth = inject(AuthService); private readonly features = inject(UserFeatureService);
  saved = false; message = '';
  readonly movie$ = this.route.paramMap.pipe(switchMap(params => this.service.getMovie(Number(params.get('id'))).pipe(catchError(() => of(null)))));
  start(id: number): void { if (!this.auth.isLoggedIn()) { this.message = 'Sign in to watch and track your progress.'; return; } this.features.recordProgress(id, 1).subscribe({ next: () => void this.router.navigate(['/watch', id]), error: () => this.message = 'Could not start playback.' }); }
  save(id: number): void { this.features.addToWatchlist(id).subscribe({ next: () => { this.saved = true; this.message = 'Added to My List.'; }, error: () => this.message = 'Could not update My List.' }); }
}
