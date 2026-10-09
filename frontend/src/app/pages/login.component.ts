import { Component, inject } from '@angular/core';
import { NgIf } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({ selector: 'app-login', standalone: true, imports: [NgIf, ReactiveFormsModule, RouterLink], template: `
  <main class="auth-page"><div class="auth-card"><p class="eyebrow">WELCOME BACK</p><h1 class="page-title">Sign in</h1><p class="auth-subtitle">Pick up where you left off.</p><form [formGroup]="form" (ngSubmit)="submit()"><label>Email<input type="email" formControlName="email" autocomplete="email"></label><label>Password<input type="password" formControlName="password" autocomplete="current-password"></label><p class="form-error" *ngIf="error">{{ error }}</p><button class="button button-primary full-width" [disabled]="form.invalid || loading">{{ loading ? 'Signing in…' : 'Sign in' }}</button></form><p class="auth-footer">New to StreamFlix? <a routerLink="/register">Create an account</a></p></div></main>
` })
export class LoginComponent {
  private readonly auth = inject(AuthService); private readonly router = inject(Router);
  readonly form = new FormGroup({ email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }), password: new FormControl('', { nonNullable: true, validators: [Validators.required] }) });
  loading = false; error = '';
  submit(): void { if (this.form.invalid) return; this.loading = true; this.error = ''; this.auth.login(this.form.controls.email.value, this.form.controls.password.value).subscribe({ next: response => { this.loading = false; if (response.profiles.length) this.auth.storeProfile(response.profiles[0]); void this.router.navigate(['/profiles']); }, error: err => { this.loading = false; this.error = err.error?.detail ?? 'Unable to sign in. Check your email and password.'; } }); }
}
