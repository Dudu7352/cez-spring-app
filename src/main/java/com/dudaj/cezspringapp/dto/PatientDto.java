package com.dudaj.cezspringapp.dto;

import com.dudaj.cezspringapp.validator.ValidPesel;
import jakarta.validation.constraints.NotBlank;

public record PatientDto(
        @ValidPesel
        String pesel,

        @NotBlank
        String name,

        @NotBlank
        String surname
) {
}
