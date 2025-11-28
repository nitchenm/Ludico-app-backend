package com.acopl.microservice_event.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acopl.microservice_event.client.clientUser;
import com.acopl.microservice_event.dto.UserDTO;
import com.acopl.microservice_event.entity.Event;
import com.acopl.microservice_event.repository.EventRepository;

@Service
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    private clientUser userClient;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event create(Event event) {
        try {
            UserDTO user = userClient.getUserById(event.getCreatorId());
            if (user == null) {
                throw new IllegalArgumentException("Creador no encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo crear el evento.");
        }

        event.setCreatedAt(LocalDateTime.now());
        Event savedEvent = eventRepository.save(event);
        return savedEvent;
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public List<Event> findByGameType(String gameType) {
        return eventRepository.findByGameType(gameType);
    }
}
