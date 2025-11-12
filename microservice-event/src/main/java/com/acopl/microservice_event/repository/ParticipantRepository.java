package com.acopl.microservice_event.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acopl.microservice_event.entity.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByEventId(Long eventId);
    boolean existsByEventIdAndUserId(Long eventId, Long userId);
    Optional<Participant> findByEventIdAndUserId(Long eventId, Long userId);
}
