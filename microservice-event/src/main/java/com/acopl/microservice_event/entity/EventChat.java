package com.acopl.microservice_event.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_chat")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;
    private Long userId;

    @Column(length = 2048)
    private String message;

    private String imagePath; // optional image attached to message

    private LocalDateTime createdAt;
}
