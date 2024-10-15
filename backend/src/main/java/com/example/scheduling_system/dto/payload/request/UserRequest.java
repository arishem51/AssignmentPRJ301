package com.example.scheduling_system.dto.payload.request;

import com.example.scheduling_system.enums.UserRole;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(@NotBlank String username, @NotBlank String password, UserRole role) {
}
