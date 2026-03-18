package com.cafeteria.kitchenservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteria.kitchenservice.document.KitchenTicket;
import com.cafeteria.kitchenservice.dto.CreateTicketRequest;
import com.cafeteria.kitchenservice.dto.StatusUpdateRequest;
import com.cafeteria.kitchenservice.service.KitchenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/kitchen")
public class KitchenController {

    private final KitchenService kitchenService;

    public KitchenController(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    /**
     * Called internally by order-service via Feign when an order is confirmed.
     * Not exposed externally through the API gateway (only lb:// internal traffic).
     */
    @PostMapping("/tickets")
    public ResponseEntity<KitchenTicket> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(kitchenService.createTicket(request));
    }

    /** Kitchen display queue — active tickets only, oldest first. */
    @GetMapping("/queue")
    public ResponseEntity<List<KitchenTicket>> getQueue() {
        return ResponseEntity.ok(kitchenService.getActiveQueue());
    }

    /** Management view — all tickets, newest first. */
    @GetMapping("/tickets")
    public ResponseEntity<List<KitchenTicket>> getAllTickets() {
        return ResponseEntity.ok(kitchenService.getAllTickets());
    }

    /** Staff updates a ticket status (PREPARING → READY → COMPLETED). */
    @PutMapping("/tickets/{ticketId}/status")
    public ResponseEntity<KitchenTicket> updateStatus(
            @PathVariable String ticketId,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(kitchenService.updateStatus(ticketId, request.getStatus()));
    }
}
