package com.acopl.microservice_event.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acopl.microservice_event.entity.Event;
import com.acopl.microservice_event.entity.Participant;
import com.acopl.microservice_event.repository.ParticipantRepository;
import com.acopl.microservice_event.service.EventService;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;
    private final ParticipantRepository participantRepository;

    public EventController(EventService eventService, ParticipantRepository participantRepository) {
        this.eventService = eventService;
        this.participantRepository = participantRepository;
    }

    @PostMapping
    public ResponseEntity<Event> create(@RequestBody Event event) {
        if (event.getCreatedAt() == null)
            event.setCreatedAt(LocalDateTime.now());
        Event saved = eventService.create(event);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Event>> listAll() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @GetMapping("/type/{gameType}")
    public ResponseEntity<List<Event>> byType(@PathVariable String gameType) {
        return ResponseEntity.ok(eventService.findByGameType(gameType));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<String> joinEvent(@PathVariable Long id) {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null)
            return ResponseEntity.status(401).body("Unauthorized");
        Long userId = Long.valueOf(auth.getName());
        if (participantRepository.existsByEventIdAndUserId(id, userId)) {
            return ResponseEntity.badRequest().body("Already joined");
        }
        Participant p = Participant.builder().eventId(id).userId(userId).joinedAt(LocalDateTime.now()).status("ACTIVE")
                .build();
        participantRepository.save(p);
        return ResponseEntity.ok("Joined");
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<String> leaveEvent(@PathVariable Long id) {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null)
            return ResponseEntity.status(401).body("Unauthorized");
        Long userId = Long.valueOf(auth.getName());
        List<Participant> list = participantRepository.findByEventId(id);
        for (Participant p : list) {
            if (p.getUserId().equals(userId)) {
                p.setStatus("LEFT");
                participantRepository.save(p);
                return ResponseEntity.ok("Left");
            }
        }
        return ResponseEntity.badRequest().body("Not participant");
    }
}
