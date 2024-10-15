package com.example.scheduling_system.dto.payload.response;

import com.example.scheduling_system.enums.UserRole;

public record UserResponse(long id, String username, UserRole role) {
}
