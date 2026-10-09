import { Component, inject } from '@angular/core';
import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { catchError, debounceTime, distinctUntilChanged, map, of, startWith, switchMap } from 'rxjs';
import { MovieService } from '../movie.service';
import { MovieCardComponent } from '../components/movie-card.component';

@Component({ selector: 'app-search', standalone: true, imports: [AsyncPipe, NgFor, NgIf, ReactiveFormsModule, MovieCardComponent], template: `
  <main class="content-shell page-space"><p class="eyebrow">FIND YOUR NEXT WATCH</p><h1 class="page-title">Search the catalogue</h1><div class="search-box"><span>⌕</span><input [formControl]="query" placeholder="Search by title or genre" aria-label="Search by title or genre"></div><ng-container *ngIf="results$ | async as results"><div class="results-heading"><h2>{{ query.value ? 'Results for “' + query.value + '”' : 'All movies' }}</h2><span>{{ results.length }} titles</span></div><div class="movie-grid"><app-movie-card *ngFor="let movie of results" [movie]="movie" /></div><p class="empty-state" *ngIf="!results.length">No titles found. Try another search.</p></ng-container></main>
` })
export class SearchComponent {
  private readonly service = inject(MovieService);
  private readonly route = inject(ActivatedRoute);
  readonly query = new FormControl('', { nonNullable: true });
  readonly results$ = this.route.queryParamMap.pipe(map(params => params.get('q') ?? ''), startWith(''), switchMap(initial => { if (initial && !this.query.value) this.query.setValue(initial, { emitEvent: false }); return this.query.valueChanges.pipe(startWith(this.query.value), debounceTime(250), distinctUntilChanged(), switchMap(query => (query ? this.service.search(query) : this.service.getMovies())), catchError(() => of([]))); }));
}
