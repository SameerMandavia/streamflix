import { Component, inject } from '@angular/core';
import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { catchError, combineLatest, debounceTime, distinctUntilChanged, map, of, startWith, switchMap } from 'rxjs';
import { MovieService } from '../movie.service';
import { MovieCardComponent } from '../components/movie-card.component';

@Component({ selector: 'app-search', standalone: true, imports: [AsyncPipe, NgFor, NgIf, ReactiveFormsModule, MovieCardComponent], template: `
  <main class="content-shell page-space"><p class="eyebrow">FIND YOUR NEXT WATCH</p><h1 class="page-title">Search the catalogue</h1><div class="search-box"><span>⌕</span><input [formControl]="query" placeholder="Search by title" aria-label="Search by title"></div><div class="filter-bar"><label>Genre<select [formControl]="genre"><option value="">All genres</option><option *ngFor="let option of genres" [value]="option">{{ option }}</option></select></label></div><ng-container *ngIf="results$ | async as results"><div class="results-heading"><h2>{{ query.value || genre.value ? 'Filtered results' : 'All movies' }}</h2><span>{{ results.length }} titles</span></div><div class="movie-grid"><app-movie-card *ngFor="let movie of results" [movie]="movie" /></div><p class="empty-state" *ngIf="!results.length">No titles found. Try another filter.</p></ng-container></main>
` })
export class SearchComponent {
  private readonly service = inject(MovieService);
  readonly query = new FormControl('', { nonNullable: true }); readonly genre = new FormControl('', { nonNullable: true });
  readonly genres = ['Sci-Fi', 'Thriller', 'Drama', 'Documentary'];
  readonly results$ = combineLatest([this.query.valueChanges.pipe(startWith(''), debounceTime(250), distinctUntilChanged()), this.genre.valueChanges.pipe(startWith(''))]).pipe(switchMap(([query, genre]) => (query || genre ? this.service.search(query, genre) : this.service.getMovies())), catchError(() => of([])));
}
