package com.acopl.microservice_event.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.acopl.microservice_event.dto.ChatMessage;
import com.acopl.microservice_event.entity.EventChat;
import com.acopl.microservice_event.entity.Participant;
import com.acopl.microservice_event.repository.EventChatRepository;
import com.acopl.microservice_event.repository.ParticipantRepository;

/**
 * WebSocket controller for real-time event chat with JWT authentication and authorization.
 * Only event participants can send and receive messages.
 */
@Controller
public class ChatController {
    private final EventChatRepository chatRepository;
    private final ParticipantRepository participantRepository;

    public ChatController(EventChatRepository chatRepository, ParticipantRepository participantRepository) {
        this.chatRepository = chatRepository;
        this.participantRepository = participantRepository;
    }

    /**
     * Handles incoming chat messages from WebSocket clients.
     * Validates JWT authentication and verifies user is an event participant.
     * 
     * @param message the chat message with eventId and content
     * @return the processed message with userId and timestamp, or error message
     */
    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public ChatMessage processMessage(@Payload ChatMessage message) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Validate authentication
        if (auth == null || auth.getName() == null) {
            message.setSentAt(LocalDateTime.now());
            message.setContent("ERROR: User not authenticated");
            return message;
        }
        
        try {
            Long userId = Long.valueOf(auth.getName());
            
            // Validate user is a participant of this event
            Participant participant = participantRepository
                .findByEventIdAndUserId(message.getEventId(), userId)
                .orElse(null);
            
            if (participant == null) {
                message.setSentAt(LocalDateTime.now());
                message.setContent("ERROR: User is not a participant of this event");
                return message;
            }
            
            // Persist message with server-side userId (preventing client-side spoofing)
            EventChat eventChat = EventChat.builder()
                    .eventId(message.getEventId())
                    .userId(userId)
                    .message(message.getContent())
                    .imagePath(message.getImagePath())
                    .createdAt(LocalDateTime.now())
                    .build();
            chatRepository.save(eventChat);
            
            // Set userId and timestamp from server-side processing
            message.setUserId(userId);
            message.setSentAt(LocalDateTime.now());
            return message;
            
        } catch (NumberFormatException e) {
            message.setSentAt(LocalDateTime.now());
            message.setContent("ERROR: Invalid user ID in token");
            return message;
        }
    }
}
