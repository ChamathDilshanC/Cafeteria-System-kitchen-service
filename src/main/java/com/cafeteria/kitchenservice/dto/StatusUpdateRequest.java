package com.cafeteria.kitchenservice.dto;

import com.cafeteria.kitchenservice.enums.TicketStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusUpdateRequest {

  @NotNull
  private TicketStatus status;
}
