import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Movie { id: number; title: string; description: string; releaseYear: number; durationMinutes: number; genre: string; posterUrl: string; }

@Injectable({ providedIn: 'root' })
export class MovieService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/movies';
  getMovies(): Observable<Movie[]> { return this.http.get<Movie[]>(this.apiUrl); }
  getTrending(): Observable<Movie[]> { return this.http.get<Movie[]>(`${this.apiUrl}/trending`); }
  getPopular(): Observable<Movie[]> { return this.http.get<Movie[]>(`${this.apiUrl}/popular`); }
  search(query: string): Observable<Movie[]> { return this.http.get<Movie[]>(`${this.apiUrl}/search`, { params: { q: query } }); }
  getMovie(id: number): Observable<Movie> { return this.http.get<Movie>(`${this.apiUrl}/${id}`); }
}
