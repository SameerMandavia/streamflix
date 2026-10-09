import { Component, inject } from '@angular/core';
import { AsyncPipe, NgIf } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { catchError, of, switchMap } from 'rxjs';
import { MovieService } from '../movie.service';

@Component({ selector: 'app-movie-details', standalone: true, imports: [AsyncPipe, NgIf, RouterLink], template: `
  <main class="details-page" *ngIf="movie$ | async as movie"><div class="details-backdrop" [style.background-image]="'url(' + movie.posterUrl + ')'" ></div><div class="details-content content-shell"><a routerLink="/" class="back-link">← Back to catalogue</a><div class="details-copy"><p class="eyebrow">{{ movie.genre }} · {{ movie.releaseYear }}</p><h1 class="page-title">{{ movie.title }}</h1><p class="details-meta">{{ movie.durationMinutes }} minutes · Feature film</p><p class="details-description">{{ movie.description }}</p><button class="button button-primary">▶ Play trailer</button></div></div></main>
  <main class="content-shell page-space empty-state" *ngIf="(movie$ | async) === null">Movie not found.</main>
` })
export class MovieDetailsComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly service = inject(MovieService);
  readonly movie$ = this.route.paramMap.pipe(switchMap(params => this.service.getMovie(Number(params.get('id'))).pipe(catchError(() => of(null)))));
}
