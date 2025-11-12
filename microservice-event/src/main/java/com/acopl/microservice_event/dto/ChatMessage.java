package com.acopl.microservice_event.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMessage {
    private Long eventId;
    private Long userId;
    private String content;
    private String imagePath; // optional
    private LocalDateTime sentAt;
}
