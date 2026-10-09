import { Component, inject } from '@angular/core';
import { AsyncPipe, NgIf } from '@angular/common';
import { catchError, forkJoin, map, of } from 'rxjs';
import { Movie, MovieService } from '../movie.service';
import { HeroBannerComponent } from '../components/hero-banner.component';
import { MovieRowComponent } from '../components/movie-row.component';

@Component({ selector: 'app-home', standalone: true, imports: [AsyncPipe, NgIf, HeroBannerComponent, MovieRowComponent], template: `
  <main><ng-container *ngIf="catalogue$ | async as catalogue"><ng-container *ngIf="catalogue.featured as featured"><app-hero-banner [movie]="featured" /></ng-container><div id="catalogue" class="content-shell"><app-movie-row title="Trending now" eyebrow="THE MOMENT" [movies]="catalogue.trending" /><app-movie-row title="Popular with members" eyebrow="TOP PICKS" [movies]="catalogue.popular" /><app-movie-row title="Recently added" eyebrow="FRESH RELEASES" [movies]="catalogue.all" /></div></ng-container></main>
` })
export class HomeComponent {
  private readonly service = inject(MovieService);
  readonly catalogue$ = forkJoin({ all: this.service.getMovies(), trending: this.service.getTrending(), popular: this.service.getPopular() }).pipe(map(catalogue => ({ ...catalogue, featured: catalogue.trending[0] ?? catalogue.all[0] })), catchError(() => of({ all: [], trending: [], popular: [], featured: undefined as Movie | undefined })));
}
