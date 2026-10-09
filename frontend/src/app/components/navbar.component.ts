import { Component } from '@angular/core';
import { NgIf } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({ selector: 'app-navbar', standalone: true, imports: [NgIf, RouterLink, RouterLinkActive], template: `
  <header class="nav shell-wide mx-auto"><a routerLink="/" class="brand">STREAM<span>FLIX</span></a><nav class="nav-links"><a routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{ exact: true }">Home</a><a href="#catalogue">Browse</a></nav><div class="nav-actions"><a class="nav-search" routerLink="/search" aria-label="Search movies"><span>⌕</span><span class="search-label">Search</span></a><ng-container *ngIf="auth.isLoggedIn(); else guest"><a class="profile-link" routerLink="/profiles">Profiles</a><button class="logout-link" type="button" (click)="auth.logout()">Log out</button></ng-container><ng-template #guest><a class="profile-link" routerLink="/login">Log in</a></ng-template></div></header>
` })
export class NavbarComponent { constructor(public readonly auth: AuthService) {} }
