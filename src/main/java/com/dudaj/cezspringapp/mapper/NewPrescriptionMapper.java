package com.dudaj.cezspringapp.mapper;

import com.dudaj.cezspringapp.dto.NewPrescriptionDto;
import com.dudaj.cezspringapp.model.NewPrescription;

public class NewPrescriptionMapper {
    public static NewPrescription fromDto(NewPrescriptionDto newPrescriptionDto) {
        return new NewPrescription(
                newPrescriptionDto.pesel(),
                newPrescriptionDto.medicineName(),
                newPrescriptionDto.doseMilligrams()
        );
    }
}
