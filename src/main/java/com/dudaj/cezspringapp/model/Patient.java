package com.dudaj.cezspringapp.model;

import com.dudaj.cezspringapp.validator.ValidPesel;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @ValidPesel
    private String pesel;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;
}
