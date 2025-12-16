package com.acopl.microservice_support.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.acopl.microservice_support.entity.SupportTicket;
import com.acopl.microservice_support.repository.SupportTicketRepository;

@Service
public class SupportService {

    private final SupportTicketRepository repository;

    public SupportService(SupportTicketRepository repository) {
        this.repository = repository;
    }

    public List<SupportTicket> getTicketsByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public SupportTicket createTicket(SupportTicket ticket) {
        if (ticket.getStatus() == null) ticket.setStatus("OPEN");
        return repository.save(ticket);
    }

    public Optional<SupportTicket> getTicketById(Long id) {
        return repository.findById(id);
    }

    public SupportTicket updateTicket(Long id, SupportTicket incoming) {
        return repository.findById(id).map(t -> {
            if (incoming.getSubject() != null) t.setSubject(incoming.getSubject());
            if (incoming.getDescription() != null) t.setDescription(incoming.getDescription());
            if (incoming.getStatus() != null) t.setStatus(incoming.getStatus());
            return repository.save(t);
        }).orElse(null);
    }

    public boolean deleteTicket(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}