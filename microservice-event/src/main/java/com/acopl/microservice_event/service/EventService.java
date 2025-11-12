package com.acopl.microservice_event.service;

import com.acopl.microservice_event.entity.Event;
import com.acopl.microservice_event.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event create(Event e) {
        e.setCreatedAt(LocalDateTime.now());
        return eventRepository.save(e);
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> findAll() { return eventRepository.findAll(); }

    public List<Event> findByGameType(String gameType) { return eventRepository.findByGameType(gameType); }
}
