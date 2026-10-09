import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movie } from './movie.service';

export interface WatchProgress { movie: Movie; progressSeconds: number; completed: boolean; }
@Injectable({ providedIn: 'root' })
export class UserFeatureService {
  private readonly http = inject(HttpClient); private readonly apiUrl = 'http://localhost:8080/api/me';
  getWatchlist(): Observable<Movie[]> { return this.http.get<Movie[]>(`${this.apiUrl}/watchlist`); }
  addToWatchlist(movieId: number): Observable<void> { return this.http.post<void>(`${this.apiUrl}/watchlist/${movieId}`, {}); }
  removeFromWatchlist(movieId: number): Observable<void> { return this.http.delete<void>(`${this.apiUrl}/watchlist/${movieId}`); }
  getContinueWatching(): Observable<WatchProgress[]> { return this.http.get<WatchProgress[]>(`${this.apiUrl}/continue-watching`); }
  getHistory(): Observable<WatchProgress[]> { return this.http.get<WatchProgress[]>(`${this.apiUrl}/history`); }
  recordProgress(movieId: number, progressSeconds: number, completed = false): Observable<WatchProgress> { return this.http.post<WatchProgress>(`${this.apiUrl}/history`, { movieId, progressSeconds, completed }); }
}
