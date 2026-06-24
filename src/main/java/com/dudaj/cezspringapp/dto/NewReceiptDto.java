package com.dudaj.cezspringapp.dto;

import com.dudaj.cezspringapp.validator.ValidPesel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record NewReceiptDto(
        @ValidPesel
        String pesel,

        @NotBlank
        String medicineName,

        @Positive
        double doseMilligrams
) {
}

