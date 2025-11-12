package com.acopl.microservice_event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acopl.microservice_event.entity.EventChat;

@Repository
public interface EventChatRepository extends JpaRepository<EventChat, Long> {
    List<EventChat> findByEventIdOrderByCreatedAtAsc(Long eventId);
}
