import { Component, Input } from '@angular/core';
import { NgFor } from '@angular/common';
import { Movie } from '../movie.service';
import { MovieCardComponent } from './movie-card.component';

@Component({ selector: 'app-movie-row', standalone: true, imports: [NgFor, MovieCardComponent], template: `
  <section class="row-section"><div class="row-heading"><div><p class="eyebrow">{{ eyebrow }}</p><h2>{{ title }}</h2></div><span class="row-arrow">→</span></div><div class="movie-row"><app-movie-card *ngFor="let movie of movies" [movie]="movie" /></div></section>
` })
export class MovieRowComponent { @Input({ required: true }) title!: string; @Input() eyebrow = 'CURATED FOR YOU'; @Input({ required: true }) movies: Movie[] = []; }
