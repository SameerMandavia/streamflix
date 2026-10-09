import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movie } from './movie.service';
import { Series } from './series.service';
export interface AdminUser { id: number; email: string; role: string; createdAt: string; }
export interface AdminSubscription { email: string; planCode: string; status: string; currentPeriodEnd: string | null; cancelAtPeriodEnd: boolean; }
export interface Analytics { movies: number; series: number; users: number; admins: number; subscriptions: number; activeSubscriptions: number; }
@Injectable({ providedIn: 'root' })
export class AdminService { private readonly http = inject(HttpClient); private readonly api = 'http://localhost:8080/api/admin'; getAnalytics(): Observable<Analytics> { return this.http.get<Analytics>(`${this.api}/analytics`); } getMovies(): Observable<Movie[]> { return this.http.get<Movie[]>(`${this.api}/movies`); } createMovie(movie: Partial<Movie>): Observable<Movie> { return this.http.post<Movie>(`${this.api}/movies`, movie); } publishMovie(id: number, value: boolean): Observable<Movie> { return this.http.patch<Movie>(`${this.api}/movies/${id}/published`, null, { params: { value } }); } uploadPoster(id: number, file: File): Observable<Movie> { const data = new FormData(); data.append('file', file); return this.http.post<Movie>(`${this.api}/movies/${id}/poster`, data); } getSeries(): Observable<Series[]> { return this.http.get<Series[]>(`${this.api}/series`); } publishSeries(id: number, value: boolean): Observable<Series> { return this.http.patch<Series>(`${this.api}/series/${id}/published`, null, { params: { value } }); } getUsers(): Observable<AdminUser[]> { return this.http.get<AdminUser[]>(`${this.api}/users`); } getSubscriptions(): Observable<AdminSubscription[]> { return this.http.get<AdminSubscription[]>(`${this.api}/subscriptions`); } }
