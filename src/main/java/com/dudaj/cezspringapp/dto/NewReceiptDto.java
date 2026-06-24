package com.dudaj.cezspringapp.dto;

public record NewReceiptDto(
        String pesel,
        String medicineName,
        double doseMilligrams
) {
}

