package com.cafeteria.kitchenservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cafeteria.kitchenservice.document.KitchenTicket;
import com.cafeteria.kitchenservice.enums.TicketStatus;

public interface KitchenTicketRepository extends MongoRepository<KitchenTicket, String> {
    List<KitchenTicket> findByStatusNotInOrderByCreatedAtAsc(List<TicketStatus> excludedStatuses);

    List<KitchenTicket> findByOrderByCreatedAtDesc();
}
