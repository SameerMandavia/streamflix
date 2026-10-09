import { Component, inject } from '@angular/core';
import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { catchError, forkJoin, map, of } from 'rxjs';
import { Movie, MovieService } from '../movie.service';
import { HeroBannerComponent } from '../components/hero-banner.component';
import { MovieRowComponent } from '../components/movie-row.component';
import { UserFeatureService, WatchProgress } from '../user-feature.service';
import { Series, SeriesService } from '../series.service';
import { AuthService } from '../auth.service';

@Component({ selector: 'app-home', standalone: true, imports: [AsyncPipe, NgFor, NgIf, RouterLink, HeroBannerComponent, MovieRowComponent], template: `
  <main><ng-container *ngIf="catalogue$ | async as catalogue"><ng-container *ngIf="catalogue.featured as featured"><app-hero-banner [movie]="featured" /></ng-container><div id="catalogue" class="content-shell"><app-movie-row title="Trending now" eyebrow="THE MOMENT" [movies]="catalogue.trending" /><app-movie-row title="Popular with members" eyebrow="TOP PICKS" [movies]="catalogue.popular" /><app-movie-row title="Recently added" eyebrow="FRESH RELEASES" [movies]="catalogue.all" /><section class="series-feature" *ngIf="series$ | async as series"><p class="eyebrow">ORIGINAL SERIES</p><a class="series-feature-link" *ngFor="let item of series" [routerLink]="['/series', item.id]"><img [src]="item.posterUrl" [alt]="item.title"><div><h2>{{ item.title }}</h2><p>{{ item.description }}</p><span>View episodes →</span></div></a></section><ng-container *ngIf="continue$ | async as continueWatching"><app-movie-row *ngIf="continueWatching.length" title="Continue watching" eyebrow="PICK UP WHERE YOU LEFT OFF" [movies]="continueWatchingMovies(continueWatching)" /></ng-container></div></ng-container></main>
` })
export class HomeComponent {
  private readonly service = inject(MovieService);
  private readonly features = inject(UserFeatureService); private readonly seriesService = inject(SeriesService); private readonly auth = inject(AuthService);
  readonly catalogue$ = forkJoin({ all: this.service.getMovies(), trending: this.service.getTrending(), popular: this.service.getPopular() }).pipe(map(catalogue => ({ ...catalogue, featured: catalogue.trending[0] ?? catalogue.all[0] })), catchError(() => of({ all: [], trending: [], popular: [], featured: undefined as Movie | undefined })));
  readonly series$ = this.seriesService.getSeries().pipe(catchError(() => of([] as Series[])));
  readonly continue$ = (this.auth.isLoggedIn() ? this.features.getContinueWatching() : of([] as WatchProgress[])).pipe(catchError(() => of([] as WatchProgress[])));
  continueWatchingMovies(items: WatchProgress[]): Movie[] { return items.map(item => item.movie); }
}
