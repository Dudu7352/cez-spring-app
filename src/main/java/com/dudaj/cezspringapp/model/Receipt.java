package com.dudaj.cezspringapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    UUID id;
    String pesel;
    String medicineName;
    double doseMilligrams;
}
