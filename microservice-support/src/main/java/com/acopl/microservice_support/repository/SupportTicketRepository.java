package com.acopl.microservice_support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acopl.microservice_support.entity.SupportTicket;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByUserId(Long userId);
}
