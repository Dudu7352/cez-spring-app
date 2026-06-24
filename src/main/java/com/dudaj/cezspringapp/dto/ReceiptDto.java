package com.dudaj.cezspringapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ReceiptDto(
        UUID id,

        @NotBlank
        String pesel,

        @NotBlank
        String medicineName,

        @Positive
        double doseMilligrams
) {
}
