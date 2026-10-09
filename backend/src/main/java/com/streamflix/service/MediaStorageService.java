package com.streamflix.service;

import com.streamflix.dto.MediaDtos;
import com.streamflix.entity.*;
import com.streamflix.repository.*;
import com.streamflix.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class MediaStorageService {
    private final Path root; private final String ffmpegPath; private final MovieRepository movies; private final VideoAssetRepository assets; private final SubtitleRepository subtitles; private final JwtService jwt;
    public MediaStorageService(@Value("${app.media.root:./media}") String root, @Value("${app.media.ffmpeg-path:ffmpeg}") String ffmpegPath, MovieRepository movies, VideoAssetRepository assets, SubtitleRepository subtitles, JwtService jwt) { this.root = Paths.get(root).toAbsolutePath().normalize(); this.ffmpegPath = ffmpegPath; this.movies = movies; this.assets = assets; this.subtitles = subtitles; this.jwt = jwt; }
    public MediaDtos.VideoAssetResponse upload(Long movieId, MultipartFile file) {
        if (file == null || file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A video file is required");
        Movie movie = movies.findById(movieId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
        try { Path dir = root.resolve("originals").resolve(movieId.toString()); Files.createDirectories(dir); String name = UUID.randomUUID() + extension(file.getOriginalFilename(), ".mp4"); Path source = dir.resolve(name).normalize(); file.transferTo(source);
            VideoAsset asset = assets.findByMovieId(movieId).orElseGet(() -> new VideoAsset(movie, root.relativize(source).toString())); asset.processing(); asset = assets.save(asset); process(asset, source); return response(asset);
        } catch (IOException ex) { throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store the video", ex); }
    }
    public MediaDtos.SubtitleResponse uploadSubtitle(Long movieId, String language, String label, MultipartFile file, String baseUrl) {
        VideoAsset asset = asset(movieId); String code = language == null || language.isBlank() ? "en" : language.replaceAll("[^a-zA-Z-]", "");
        if (file == null || file.isEmpty() || !file.getOriginalFilename().toLowerCase().endsWith(".vtt")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subtitle must be a non-empty .vtt file");
        try { Path dir = root.resolve("subtitles").resolve(movieId.toString()); Files.createDirectories(dir); Path target = dir.resolve(code + ".vtt"); file.transferTo(target); Subtitle subtitle = new Subtitle(asset, code, label == null || label.isBlank() ? code : label, root.relativize(target).toString()); subtitles.save(subtitle); return new MediaDtos.SubtitleResponse(subtitle.getLanguageCode(), subtitle.getLabel(), baseUrl + "/api/media/movies/" + movieId + "/subtitles/" + code + "?token=UPLOAD_TOKEN"); }
        catch (IOException ex) { throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store subtitles", ex); }
    }
    public MediaDtos.PlaybackResponse playback(Long movieId, String email, String baseUrl) { VideoAsset asset = asset(movieId); if (!"READY".equals(asset.getStatus())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Video is not ready for playback"); String token = jwt.issuePlaybackToken(email, movieId); List<MediaDtos.SubtitleResponse> tracks = subtitles.findByVideoAssetIdOrderByLanguageCode(asset.getId()).stream().map(s -> new MediaDtos.SubtitleResponse(s.getLanguageCode(), s.getLabel(), baseUrl + "/api/media/movies/" + movieId + "/subtitles/" + s.getLanguageCode() + "?token=" + token)).toList(); return new MediaDtos.PlaybackResponse(baseUrl + "/api/media/movies/" + movieId + "/hls/master.m3u8?token=" + token, tracks); }
    public ResponseEntity<?> hls(Long movieId, String path, String token) { if (!jwt.isPlaybackTokenFor(token, movieId)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid playback token"); VideoAsset asset = asset(movieId); Path hlsRoot = root.resolve(asset.getHlsPath()).normalize(); Path target = hlsRoot.resolve(path).normalize(); if (!target.startsWith(hlsRoot) || !Files.exists(target)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Media segment not found"); try { String type = target.toString().endsWith(".m3u8") ? "application/vnd.apple.mpegurl" : "video/mp2t"; if (target.toString().endsWith(".m3u8")) { String playlist = Files.readString(target); String rewritten = java.util.Arrays.stream(playlist.split("\\R", -1)).map(line -> line.isBlank() || line.startsWith("#") ? line : "/api/media/movies/" + movieId + "/hls/" + hlsRoot.relativize(target.getParent().resolve(line)).toString().replace('\\', '/') + "?token=" + token).reduce((a,b) -> a + "\\n" + b).orElse(playlist); return ResponseEntity.ok().contentType(MediaType.parseMediaType(type)).body(rewritten); } return ResponseEntity.ok().contentType(MediaType.parseMediaType(type)).body(new ByteArrayResource(Files.readAllBytes(target))); } catch (IOException ex) { throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not read media", ex); } }
    public ResponseEntity<ByteArrayResource> subtitle(Long movieId, String language, String token) { if (!jwt.isPlaybackTokenFor(token, movieId)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid playback token"); VideoAsset asset = asset(movieId); Subtitle subtitle = subtitles.findByVideoAssetIdOrderByLanguageCode(asset.getId()).stream().filter(s -> s.getLanguageCode().equals(language)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subtitle not found")); try { return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(new ByteArrayResource(Files.readAllBytes(root.resolve(subtitle.getFilePath()).normalize()))); } catch (IOException ex) { throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not read subtitle", ex); } }
    private VideoAsset asset(Long movieId) { return assets.findByMovieId(movieId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No video asset found")); }
    private MediaDtos.VideoAssetResponse response(VideoAsset a) { return new MediaDtos.VideoAssetResponse(a.getMovie().getId(), a.getStatus(), a.getErrorMessage()); }
    private void process(VideoAsset asset, Path source) { Path output = root.resolve("hls").resolve(asset.getMovie().getId().toString()); try { Files.createDirectories(output.resolve("720p")); Files.createDirectories(output.resolve("480p")); runFfmpeg(source, output.resolve("720p/index.m3u8"), output.resolve("720p/segment%03d.ts"), "720", "2500k"); runFfmpeg(source, output.resolve("480p/index.m3u8"), output.resolve("480p/segment%03d.ts"), "480", "900k"); Files.writeString(output.resolve("master.m3u8"), "#EXTM3U\\n#EXT-X-VERSION:3\\n#EXT-X-STREAM-INF:BANDWIDTH=2800000,RESOLUTION=1280x720\\n720p/index.m3u8\\n#EXT-X-STREAM-INF:BANDWIDTH=1100000,RESOLUTION=854x480\\n480p/index.m3u8\\n", StandardCharsets.UTF_8); asset.ready(root.relativize(output).toString()); assets.save(asset); } catch (Exception ex) { asset.failed(ex.getMessage() == null ? "FFmpeg processing failed" : ex.getMessage()); assets.save(asset); } }
    private void runFfmpeg(Path source, Path playlist, Path segments, String height, String bitrate) throws IOException, InterruptedException { Process process = new ProcessBuilder(ffmpegPath, "-y", "-i", source.toString(), "-vf", "scale=-2:" + height, "-c:v", "libx264", "-preset", "veryfast", "-b:v", bitrate, "-c:a", "aac", "-b:a", "128k", "-f", "hls", "-hls_time", "6", "-hls_playlist_type", "vod", "-hls_segment_filename", segments.toString(), playlist.toString()).redirectErrorStream(true).start(); String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8); if (process.waitFor() != 0) throw new IllegalStateException(output.length() > 900 ? output.substring(output.length() - 900) : output); }
    private String extension(String name, String fallback) { if (name == null || !name.contains(".")) return fallback; String ext = name.substring(name.lastIndexOf('.')).replaceAll("[^a-zA-Z0-9.]", ""); return ext.length() > 8 ? fallback : ext; }
}
