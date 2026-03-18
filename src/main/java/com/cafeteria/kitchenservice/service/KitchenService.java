package com.cafeteria.kitchenservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cafeteria.kitchenservice.document.KitchenTicket;
import com.cafeteria.kitchenservice.dto.CreateTicketRequest;
import com.cafeteria.kitchenservice.enums.TicketStatus;
import com.cafeteria.kitchenservice.repository.KitchenTicketRepository;

@Service
public class KitchenService {

    private final KitchenTicketRepository ticketRepository;
    private final OrderClient orderClient;

    public KitchenService(KitchenTicketRepository ticketRepository, OrderClient orderClient) {
        this.ticketRepository = ticketRepository;
        this.orderClient = orderClient;
    }

    /** Called by order-service via Feign when an order is confirmed. */
    public KitchenTicket createTicket(CreateTicketRequest request) {
        KitchenTicket ticket = new KitchenTicket();
        ticket.setOrderId(request.getOrderId());
        ticket.setUserId(request.getUserId());
        ticket.setNotes(request.getNotes());

        if (request.getItems() != null) {
            request.getItems().forEach(dto -> {
                KitchenTicket.TicketItem item = new KitchenTicket.TicketItem();
                item.setMenuItemId(dto.getMenuItemId());
                item.setItemName(dto.getItemName());
                item.setQuantity(dto.getQuantity());
                ticket.getItems().add(item);
            });
        }

        ticket.getStatusHistory().add(new KitchenTicket.StatusLog(TicketStatus.PENDING));
        return ticketRepository.save(ticket);
    }

    /**
     * Returns active tickets (not COMPLETED/CANCELLED), sorted oldest-first —
     * kitchen display.
     */
    public List<KitchenTicket> getActiveQueue() {
        return ticketRepository.findByStatusNotInOrderByCreatedAtAsc(
                List.of(TicketStatus.COMPLETED, TicketStatus.CANCELLED));
    }

    /** Returns all tickets in reverse chronological order — management view. */
    public List<KitchenTicket> getAllTickets() {
        return ticketRepository.findByOrderByCreatedAtDesc();
    }

    /** Updates ticket status and appends a log entry. */
    public KitchenTicket updateStatus(String ticketId, TicketStatus newStatus) {
        KitchenTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketId));

        ticket.setStatus(newStatus);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.getStatusHistory().add(new KitchenTicket.StatusLog(newStatus));

        KitchenTicket saved = ticketRepository.save(ticket);

        // Sync order status for terminal states
        if (newStatus == TicketStatus.CANCELLED || newStatus == TicketStatus.COMPLETED
                || newStatus == TicketStatus.PREPARING || newStatus == TicketStatus.READY) {
            try {
                orderClient.updateOrderStatus(saved.getOrderId(), Map.of("status", newStatus.name()));
            } catch (Exception ignored) {
                // Non-critical — order sync failure should not break kitchen update
            }
        }

        return saved;
    }
}
