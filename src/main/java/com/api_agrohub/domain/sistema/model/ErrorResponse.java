package com.api_agrohub.domain.sistema.model;


import java.time.LocalDateTime;

public record ErrorResponse(
    String message,
    int status,
    LocalDateTime timestamp
) {}