import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
export interface Episode { id: number; episodeNumber: number; title: string; description: string; durationMinutes: number; }
export interface Season { id: number; seasonNumber: number; title: string; episodes: Episode[]; }
export interface Series { id: number; title: string; description: string; releaseYear: number; genre: string; posterUrl: string; seasons: Season[]; published: boolean; }
@Injectable({ providedIn: 'root' })
export class SeriesService { private readonly http = inject(HttpClient); private readonly apiUrl = 'http://localhost:8080/api/series'; getSeries(): Observable<Series[]> { return this.http.get<Series[]>(this.apiUrl); } getSeriesById(id: number): Observable<Series> { return this.http.get<Series>(`${this.apiUrl}/${id}`); } }
