package com.dudaj.cezspringapp.model;

import com.dudaj.cezspringapp.validator.ValidPesel;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewReceipt {
    @ValidPesel
    String pesel;

    @NotBlank
    String medicineName;

    @NotBlank
    double doseMilligrams;
}