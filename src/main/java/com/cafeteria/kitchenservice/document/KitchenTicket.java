package com.cafeteria.kitchenservice.document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cafeteria.kitchenservice.enums.TicketStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "kitchen_tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KitchenTicket {

  @Id
  private String id;

  private Long orderId;
  private Long userId;

  @Default
  private List<TicketItem> items = new ArrayList<>();

  @Default
  private TicketStatus status = TicketStatus.PENDING;

  private String notes;

  @Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @Default
  private LocalDateTime updatedAt = LocalDateTime.now();

  /** Immutable log of every status transition. */
  @Default
  private List<StatusLog> statusHistory = new ArrayList<>();

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class TicketItem {
    private Long menuItemId;
    private String itemName;
    private int quantity;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class StatusLog {
    private TicketStatus status;
    private LocalDateTime changedAt;

    public StatusLog(TicketStatus status) {
      this.status = status;
      this.changedAt = LocalDateTime.now();
    }
  }
}
