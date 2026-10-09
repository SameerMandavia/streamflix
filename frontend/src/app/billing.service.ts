import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
export interface Plan { code: string; name: string; priceCents: number; currency: string; billingInterval: string; premium: boolean; }
export interface SubscriptionStatus { planCode: string; planName: string; status: string; currentPeriodEnd: string | null; cancelAtPeriodEnd: boolean; premium: boolean; }
@Injectable({ providedIn: 'root' })
export class BillingService { private readonly http = inject(HttpClient); private readonly apiUrl = 'http://localhost:8080/api/subscriptions'; getPlans(): Observable<Plan[]> { return this.http.get<Plan[]>(`${this.apiUrl}/plans`); } getCurrent(): Observable<SubscriptionStatus> { return this.http.get<SubscriptionStatus>(`${this.apiUrl}/me`); } checkout(planCode: string): Observable<{ url: string }> { return this.http.post<{ url: string }>(`${this.apiUrl}/checkout`, { planCode }); } cancel(): Observable<void> { return this.http.post<void>(`${this.apiUrl}/cancel`, {}); } }
