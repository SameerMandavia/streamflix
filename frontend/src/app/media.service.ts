import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
export interface SubtitleTrack { languageCode: string; label: string; url: string; }
export interface Playback { manifestUrl: string; subtitles: SubtitleTrack[]; }
@Injectable({ providedIn: 'root' })
export class MediaService { private readonly http = inject(HttpClient); private readonly apiUrl = 'http://localhost:8080/api/media/movies'; getPlayback(movieId: number): Observable<Playback> { return this.http.get<Playback>(`${this.apiUrl}/${movieId}/playback-token`); } }
