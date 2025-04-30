package ru.pin36bik.dto;

import java.util.List;

public record TokenValidationResponse(
        boolean isValid,
        String email,
        List<String> roles
) {}
