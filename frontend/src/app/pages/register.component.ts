import { Component, inject } from '@angular/core';
import { NgIf } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({ selector: 'app-register', standalone: true, imports: [NgIf, ReactiveFormsModule, RouterLink], template: `
  <main class="auth-page"><div class="auth-card"><p class="eyebrow">START YOUR JOURNEY</p><h1 class="page-title">Create account</h1><p class="auth-subtitle">One account. Every story.</p><form [formGroup]="form" (ngSubmit)="submit()"><label>Email<input type="email" formControlName="email" autocomplete="email"></label><label>Password<input type="password" formControlName="password" autocomplete="new-password"><small>At least 8 characters</small></label><label>First profile name<input type="text" formControlName="profileName" autocomplete="nickname"></label><p class="form-error" *ngIf="error">{{ error }}</p><button class="button button-primary full-width" [disabled]="form.invalid || loading">{{ loading ? 'Creating account…' : 'Create account' }}</button></form><p class="auth-footer">Already a member? <a routerLink="/login">Sign in</a></p></div></main>
` })
export class RegisterComponent {
  private readonly auth = inject(AuthService); private readonly router = inject(Router);
  readonly form = new FormGroup({ email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }), password: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(8)] }), profileName: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(2)] }) });
  loading = false; error = '';
  submit(): void { if (this.form.invalid) return; this.loading = true; this.error = ''; this.auth.register(this.form.controls.email.value, this.form.controls.password.value, this.form.controls.profileName.value).subscribe({ next: response => { this.loading = false; if (response.profiles.length) this.auth.storeProfile(response.profiles[0]); void this.router.navigate(['/profiles']); }, error: err => { this.loading = false; this.error = err.error?.detail ?? 'Unable to create your account.'; } }); }
}
