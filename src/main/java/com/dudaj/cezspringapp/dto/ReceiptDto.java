package com.dudaj.cezspringapp.dto;

import java.util.UUID;

public record ReceiptDto(
        UUID id,
        String pesel,
        String medicineName,
        double doseMilligrams
) {
}
