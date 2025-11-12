package com.acopl.microservice_event.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acopl.microservice_event.entity.EventChat;
import com.acopl.microservice_event.repository.EventChatRepository;

@Service
public class ChatService {
    private final EventChatRepository chatRepository;

    public ChatService(EventChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<EventChat> findByEvent(Long eventId) {
        return chatRepository.findByEventIdOrderByCreatedAtAsc(eventId);
    }
}
