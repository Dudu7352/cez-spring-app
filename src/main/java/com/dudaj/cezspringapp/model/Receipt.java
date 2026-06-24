package com.dudaj.cezspringapp.model;

import com.dudaj.cezspringapp.validator.ValidPesel;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    UUID id;
    
    @ValidPesel
    String pesel;

    @NotBlank
    String medicineName;

    @NotBlank
    double doseMilligrams;
}
