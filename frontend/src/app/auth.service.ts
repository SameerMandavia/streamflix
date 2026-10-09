import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

export interface Profile { id: number; name: string; avatarKey: string; }
export interface AuthResponse { token: string; email: string; role: string; profiles: Profile[]; }

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient); private readonly router = inject(Router); private readonly apiUrl = 'http://localhost:8080/api';
  readonly tokenKey = 'streamflix.token'; readonly emailKey = 'streamflix.email'; readonly roleKey = 'streamflix.role'; readonly profileKey = 'streamflix.profile';
  register(email: string, password: string, profileName: string): Observable<AuthResponse> { return this.http.post<AuthResponse>(`${this.apiUrl}/auth/register`, { email, password, profileName }).pipe(tap(response => this.store(response))); }
  login(email: string, password: string): Observable<AuthResponse> { return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, { email, password }).pipe(tap(response => this.store(response))); }
  getProfiles(): Observable<Profile[]> { return this.http.get<Profile[]>(`${this.apiUrl}/profiles`); }
  createProfile(name: string, avatarKey = 'sunset'): Observable<Profile> { return this.http.post<Profile>(`${this.apiUrl}/profiles`, { name, avatarKey }); }
  storeProfile(profile: Profile): void { localStorage.setItem(this.profileKey, JSON.stringify(profile)); }
  currentProfile(): Profile | null { const value = localStorage.getItem(this.profileKey); return value ? JSON.parse(value) as Profile : null; }
  isLoggedIn(): boolean { return !!localStorage.getItem(this.tokenKey); }
  isAdmin(): boolean { return localStorage.getItem(this.roleKey) === 'ADMIN'; }
  logout(): void { localStorage.removeItem(this.tokenKey); localStorage.removeItem(this.emailKey); localStorage.removeItem(this.roleKey); localStorage.removeItem(this.profileKey); void this.router.navigate(['/login']); }
  private store(response: AuthResponse): void { localStorage.setItem(this.tokenKey, response.token); localStorage.setItem(this.emailKey, response.email); localStorage.setItem(this.roleKey, response.role); }
}
