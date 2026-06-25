package com.dudaj.cezspringapp.model;

import com.dudaj.cezspringapp.validator.ValidPesel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPrescription {
    @ValidPesel
    String pesel;

    @NotBlank
    String medicineName;

    @Positive
    double doseMilligrams;
}