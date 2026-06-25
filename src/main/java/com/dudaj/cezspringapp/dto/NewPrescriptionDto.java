package com.dudaj.cezspringapp.dto;

import com.dudaj.cezspringapp.validator.ValidPesel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record NewPrescriptionDto(
        @ValidPesel
        String pesel,

        @NotBlank
        String medicineName,

        @Positive
        double doseMilligrams
) {
}

