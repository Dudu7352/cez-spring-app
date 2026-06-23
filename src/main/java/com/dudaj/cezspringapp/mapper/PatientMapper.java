package com.dudaj.cezspringapp.mapper;

import com.dudaj.cezspringapp.dto.PatientDto;
import com.dudaj.cezspringapp.model.Patient;

public class PatientMapper {
    public static PatientDto toDto(Patient patient) {
        return new PatientDto(patient.getPesel(), patient.getName(), patient.getSurname());
    }

    public static Patient fromDto(PatientDto patientDto) {
        return new Patient(patientDto.pesel(), patientDto.name(), patientDto.surname());
    }
}
