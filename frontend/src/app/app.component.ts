import { Component, inject } from '@angular/core';
import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { Observable, catchError, of } from 'rxjs';
import { Movie, MovieService } from './movie.service';

@Component({ selector: 'app-root', standalone: true, imports: [AsyncPipe, NgFor, NgIf], templateUrl: './app.component.html' })
export class AppComponent {
  private readonly movieService = inject(MovieService);
  readonly movies$: Observable<Movie[]> = this.movieService.getMovies().pipe(catchError(() => of([])));
}
