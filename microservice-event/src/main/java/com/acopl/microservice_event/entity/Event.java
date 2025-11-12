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
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2048)
    private String description;

    private String gameType; // MTG, DnD, CardGame, etc.

    private Double latitude;
    private Double longitude;

    private LocalDateTime startsAt;
    private LocalDateTime endsAt;

    private Integer capacity;

    private String mainImagePath;

    private Long creatorId; // FK to user service (not implemented yet)

    private LocalDateTime createdAt;
}
