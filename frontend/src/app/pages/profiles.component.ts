import { Component, inject } from '@angular/core';
import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, of } from 'rxjs';
import { AuthService, Profile } from '../auth.service';

@Component({ selector: 'app-profiles', standalone: true, imports: [AsyncPipe, NgFor, NgIf, ReactiveFormsModule], template: `
  <main class="profile-page content-shell"><p class="eyebrow">WHO'S WATCHING?</p><h1 class="page-title">Choose a profile</h1><div class="profile-grid"><button class="profile-card" *ngFor="let profile of profiles$ | async" type="button" (click)="select(profile)"><span class="avatar" [attr.data-avatar]="profile.avatarKey">{{ profile.name.charAt(0).toUpperCase() }}</span><span>{{ profile.name }}</span></button><button class="profile-card add-profile" type="button" (click)="showForm = !showForm"><span class="avatar add-avatar">+</span><span>Add profile</span></button></div><form class="add-profile-form" *ngIf="showForm" (ngSubmit)="addProfile()"><input [formControl]="name" placeholder="Profile name" aria-label="New profile name"><button class="button button-primary" [disabled]="name.invalid">Save profile</button><p class="form-error" *ngIf="error">{{ error }}</p></form></main>
` })
export class ProfilesComponent {
  private readonly auth = inject(AuthService); private readonly router = inject(Router);
  readonly profiles$ = this.auth.getProfiles().pipe(catchError(() => of([] as Profile[])));
  readonly name = new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(2)] });
  showForm = false; error = '';
  select(profile: Profile): void { this.auth.storeProfile(profile); void this.router.navigate(['/']); }
  addProfile(): void { if (this.name.invalid) return; this.auth.createProfile(this.name.value).subscribe({ next: profile => { this.auth.storeProfile(profile); this.showForm = false; this.name.reset(); window.location.reload(); }, error: err => this.error = err.error?.detail ?? 'Could not create profile.' }); }
}
