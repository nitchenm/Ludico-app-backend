package com.acopl.microservice_support.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acopl.microservice_support.entity.SupportTicket;
import com.acopl.microservice_support.service.SupportService;

@RestController
@RequestMapping("/api/v1/support")
public class SupportController {

    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    @PostMapping
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
    }
}