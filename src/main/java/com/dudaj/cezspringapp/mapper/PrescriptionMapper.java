package com.dudaj.cezspringapp.mapper;

import com.dudaj.cezspringapp.dto.PrescriptionDto;
import com.dudaj.cezspringapp.model.Prescription;

public class PrescriptionMapper {
    public static PrescriptionDto toDto(Prescription prescription) {
        return new PrescriptionDto(
                prescription.getId(),
                prescription.getPesel(),
                prescription.getMedicineName(),
                prescription.getDoseMilligrams()
        );
    }

    public static Prescription fromDto(Prescription prescription) {
        return new Prescription(
                prescription.getId(),
                prescription.getPesel(),
                prescription.getMedicineName(),
                prescription.getDoseMilligrams()
        );
    }
}
