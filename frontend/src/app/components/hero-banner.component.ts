import { Component, Input } from "@angular/core";
import { RouterLink } from "@angular/router";
import { Movie } from "../movie.service";

@Component({
  selector: "app-hero-banner",
  standalone: true,
  imports: [RouterLink],
  template: `
    <section
      class="hero shell-wide"
      [style.--hero-image]="'url(' + movie.posterUrl + ')'"
    >
      <div class="hero-copy">
        <p class="eyebrow">STREAMFLIX ORIGINAL PICK</p>
        <h1>{{ movie.title }}</h1>
        <div class="hero-meta">
          <span>{{ movie.releaseYear }}</span
          ><span>{{ movie.genre }}</span
          ><span>{{ movie.durationMinutes }} min</span>
        </div>
        <p class="hero-description">{{ movie.description }}</p>
        <a class="button button-primary" [routerLink]="['/movies', movie.id]"
          >View details <span>→</span></a
        >
      </div>
    </section>
  `,
})
export class HeroBannerComponent {
  @Input({ required: true }) movie!: Movie;
}
