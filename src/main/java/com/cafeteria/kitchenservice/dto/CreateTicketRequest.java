package com.cafeteria.kitchenservice.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTicketRequest {

  @NotNull
  private Long orderId;

  @NotNull
  private Long userId;

  private List<TicketItemDto> items;

  private String notes;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class TicketItemDto {
    private Long menuItemId;
    private String itemName;
    private int quantity;
  }
}
