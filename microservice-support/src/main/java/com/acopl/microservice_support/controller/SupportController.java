package com.acopl.microservice_support.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import com.acopl.microservice_support.entity.SupportTicket;
import com.acopl.microservice_support.service.SupportService;

@RestController
@RequestMapping("/api/v1/support")
public class SupportController {

    private final SupportService service;

    public SupportController(SupportService service) {
        this.service = service;
    }

    @PostMapping
<<<<<<< HEAD
    public ResponseEntity<SupportTicket> create(@RequestBody SupportTicket ticket) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            try {
                ticket.setUserId(Long.valueOf(auth.getName()));
            } catch (NumberFormatException ignored) {}
        }
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
=======
    public ResponseEntity<SupportTicket> createTicket(@RequestBody SupportTicket ticket) {
        // Extraer el ID del usuario autenticado desde el Token JWT
        /*
         * var auth = SecurityContextHolder.getContext().getAuthentication();
         * if (auth != null && auth.getName() != null) {
         * try {
         * Long userId = Long.valueOf(auth.getName());
         * ticket.setUserId(userId);
         * } catch (NumberFormatException e) {
         * // Manejar caso anónimo o error
         * }
         * }
         */
        SupportTicket created = supportService.createTicket(ticket);
        return ResponseEntity.ok(created);
>>>>>>> 7331d966400d5999f85249bc7affc6f355828f3e
    }
}