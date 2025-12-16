package com.acopl.microservice_support.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.acopl.microservice_support.entity.SupportTicket;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acopl.microservice_support.entity.SupportTicket;
import com.acopl.microservice_support.service.SupportService;

import feign.Response;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/support")
public class SupportController {

    private final SupportService service;

    public SupportController(SupportService service) {
        this.service = service;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SupportTicket>> getByUser(@PathVariable Long userId) {
        List<SupportTicket> tickets = service.getTicketsByUserId(userId);
        return tickets.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tickets);
    }

    @GetMapping
    public ResponseEntity<List<SupportTicket>> listAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<SupportTicket> create(@RequestBody SupportTicket ticket) {
        return ResponseEntity.ok(service.createTicket(ticket));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicket> get(@PathVariable Long id) {
        return service.getTicketById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupportTicket> update(@PathVariable Long id, @RequestBody SupportTicket body) {
        var updated = service.updateTicket(id, body);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.deleteTicket(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();

    }
}