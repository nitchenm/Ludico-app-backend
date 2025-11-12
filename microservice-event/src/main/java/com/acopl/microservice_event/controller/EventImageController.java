package com.acopl.microservice_event.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.acopl.microservice_event.entity.EventImage;
import com.acopl.microservice_event.repository.EventImageRepository;
import com.acopl.microservice_event.service.FileStorageService;

@RestController
@RequestMapping("/api/events/{eventId}/images")
public class EventImageController {
    private final FileStorageService fileStorageService;
    private final EventImageRepository imageRepository;

    public EventImageController(FileStorageService fileStorageService, EventImageRepository imageRepository) {
        this.fileStorageService = fileStorageService;
        this.imageRepository = imageRepository;
    }

    @PostMapping
    public ResponseEntity<?> upload(@PathVariable Long eventId, @RequestParam("file") MultipartFile file, @RequestParam(required = false) Long userId, @RequestParam(required = false) String description) {
        try {
            // if userId not provided, extract from JWT
            if (userId == null) {
                var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.getName() != null) {
                    try { userId = Long.valueOf(auth.getName()); } catch (Exception ignored) {}
                }
            }
            String path = fileStorageService.storeEventImage(eventId, file);
            EventImage img = EventImage.builder().eventId(eventId).userId(userId).path(path).description(description).uploadedAt(LocalDateTime.now()).build();
            imageRepository.save(img);
            return ResponseEntity.ok(img);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
