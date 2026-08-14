package com.quitto.server.application.dto;

public record ErrorResponse(
        String title,
        String msg
    ) {
}
