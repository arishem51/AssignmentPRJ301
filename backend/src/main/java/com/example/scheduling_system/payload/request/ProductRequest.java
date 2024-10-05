package com.example.scheduling_system.payload.request;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(@NotBlank String name, @NotBlank Double estimatedEffort, @NotBlank String img) {

}
