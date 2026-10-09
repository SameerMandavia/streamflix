import { Component, inject } from '@angular/core';
import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { catchError, of } from 'rxjs';
import { UserFeatureService, WatchProgress } from '../user-feature.service';
import { MovieCardComponent } from '../components/movie-card.component';
@Component({ selector: 'app-history', standalone: true, imports: [AsyncPipe, NgFor, NgIf, MovieCardComponent], template: `<main class="content-shell page-space"><p class="eyebrow">YOUR VIEWING ACTIVITY</p><h1 class="page-title">Watch history</h1><ng-container *ngIf="history$ | async as history"><div class="movie-grid"><app-movie-card *ngFor="let item of history" [movie]="item.movie" /></div><p class="empty-state" *ngIf="!history.length">Your watch history will appear here.</p></ng-container></main>` })
export class HistoryComponent { private readonly features = inject(UserFeatureService); readonly history$ = this.features.getHistory().pipe(catchError(() => of([] as WatchProgress[]))); }
