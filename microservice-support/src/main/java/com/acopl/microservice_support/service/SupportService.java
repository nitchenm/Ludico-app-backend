package com.acopl.microservice_support.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.acopl.microservice_support.entity.SupportTicket;
import com.acopl.microservice_support.repository.SupportTicketRepository;

@Service
public class SupportService {

    private final SupportTicketRepository repository;

    public SupportService(SupportTicketRepository repository) {
        this.repository = repository;
    }

    public SupportTicket createTicket(SupportTicket ticket) {
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setStatus("OPEN");
        return repository.save(ticket);
    }
}