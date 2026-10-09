import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { catchError, of, switchMap } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import Hls from 'hls.js';
import { MediaService, Playback } from '../media.service';

@Component({ selector: 'app-video-player', standalone: true, imports: [NgFor, NgIf, RouterLink], template: `<main class="player-page content-shell"><a routerLink="/" class="back-link">← Back to catalogue</a><ng-container *ngIf="playback; else loading"><video #video class="video-player" controls autoplay playsinline><track *ngFor="let track of playback.subtitles" [src]="track.url" [srclang]="track.languageCode" [label]="track.label"></video><div class="player-meta"><p class="eyebrow">NOW PLAYING</p><h1 class="page-title">StreamFlix player</h1><p>Adaptive HLS playback is active. Use the player quality menu to switch available renditions.</p></div></ng-container><ng-template #loading><p class="empty-state">Preparing secure playback…</p></ng-template><p class="form-error" *ngIf="error">{{ error }}</p></main>` })
export class VideoPlayerComponent implements OnInit, AfterViewInit, OnDestroy {
  private readonly route = inject(ActivatedRoute); private readonly media = inject(MediaService); private hls?: Hls;
  @ViewChild('video') video?: ElementRef<HTMLVideoElement>; playback?: Playback; error = ''; private pendingUrl = '';
  ngOnInit(): void { this.route.paramMap.pipe(switchMap(params => this.media.getPlayback(Number(params.get('movieId'))).pipe(catchError((error: HttpErrorResponse) => { this.error = error.status === 402 ? 'An active subscription is required to watch this title.' : error.status === 404 ? 'This title is not ready for playback yet.' : 'Playback is temporarily unavailable.'; return of(null); })))).subscribe(playback => { if (!playback) return; this.playback = playback; this.pendingUrl = playback.manifestUrl; setTimeout(() => this.attachPlayer()); }); }
  ngAfterViewInit(): void { this.attachPlayer(); }
  ngOnDestroy(): void { this.hls?.destroy(); }
  private attachPlayer(): void { if (!this.video || !this.pendingUrl) return; const element = this.video.nativeElement; if (Hls.isSupported()) { this.hls = new Hls({ enableWorker: true }); this.hls.loadSource(this.pendingUrl); this.hls.attachMedia(element); } else if (element.canPlayType('application/vnd.apple.mpegurl')) element.src = this.pendingUrl; }
}
