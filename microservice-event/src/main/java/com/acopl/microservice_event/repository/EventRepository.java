package com.acopl.microservice_event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acopl.microservice_event.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    List<Event> findByGameType(String gameType);
}
