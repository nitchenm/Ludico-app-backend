package com.acopl.microservice_event.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    @Value("${event.file.storage:uploads}")
    private String storageRoot;

    @Value("${event.file.max-size-bytes:5242880}")
    private long maxSizeBytes; // default 5MB

    public String storeEventImage(Long eventId, MultipartFile file) throws IOException {
        if (file.getSize() > maxSizeBytes) {
            throw new IOException("File too large");
        }
        Path eventDir = Path.of(storageRoot, "events", String.valueOf(eventId));
        Files.createDirectories(eventDir);
        String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path target = eventDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
    }
}
