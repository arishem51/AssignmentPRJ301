package com.example.scheduling_system.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlanProductItemRequest(@NotBlank String name, @NotNull int productId, @NotNull int quantity) {
}
