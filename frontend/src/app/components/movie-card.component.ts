import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Movie } from '../movie.service';

@Component({ selector: 'app-movie-card', standalone: true, imports: [RouterLink], template: `
  <a class="movie-card" [routerLink]="['/movies', movie.id]"><div class="poster-wrap"><img [src]="movie.posterUrl" [alt]="movie.title + ' poster'" loading="lazy"><span class="play-icon">▶</span></div><div class="movie-card-info"><div class="meta"><span>{{ movie.genre }}</span><span>{{ movie.releaseYear }}</span></div><h3>{{ movie.title }}</h3></div></a>
` })
export class MovieCardComponent { @Input({ required: true }) movie!: Movie; }
