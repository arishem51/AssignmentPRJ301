package com.example.scheduling_system.dto.payload.request;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(@NotBlank String name, @NotBlank String img) {

}
