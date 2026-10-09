import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Movie { id: number; title: string; description: string; releaseYear: number; durationMinutes: number; genre: string; posterUrl: string; }

@Injectable({ providedIn: 'root' })
export class MovieService {
  private readonly http = inject(HttpClient);
  getMovies(): Observable<Movie[]> { return this.http.get<Movie[]>('http://localhost:8080/api/movies'); }
}
