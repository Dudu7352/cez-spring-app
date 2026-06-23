package com.dudaj.cezspringapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewReceipt {
    String pesel;
    String medicineName;
    double doseMilligrams;
}