package com.dudaj.cezspringapp.service;

import com.dudaj.cezspringapp.dto.PatientDto;
import com.dudaj.cezspringapp.mapper.PatientMapper;
import com.dudaj.cezspringapp.model.Patient;
import com.dudaj.cezspringapp.repository.PatientRepository;
import com.dudaj.cezspringapp.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    @Autowired
    private final PatientRepository patientRepository;
    @Autowired
    private final ReceiptRepository receiptRepository;

    /**
     * Retrieves a patient with the specified PESEL number.
     *
     * @param pesel patient's PESEL
     * @return On success, a patient is returned wrapped inside Optional container. If none were found, an empty list is returned
     */
    Optional<PatientDto> getPatient(String pesel) {
        return patientRepository.findByPesel(pesel).map(PatientMapper::toDto);
    }

    /**
     * Returns all patients
     *
     * @return a list of all patients
     */
    List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream().map(PatientMapper::toDto).toList();
    }

    /**
     * Adds a new patient.
     *
     * @param newPatientDto new patient
     * @return On success, added patient is returned wrapped inside Optional container. On failure, an empty optional is returned
     */
    Optional<PatientDto> addPatient(PatientDto newPatientDto) {
        Patient newPatient = PatientMapper.fromDto(newPatientDto);
        return patientRepository.save(newPatient).map(PatientMapper::toDto);
    }

    /**
     * Removes a patient and every receipt associated with his PESEL. This action is idempotent.
     *
     * @param pesel patient's PESEL
     */
    void removePatient(String pesel) {
        patientRepository.deleteByPesel(pesel);
        receiptRepository.deleteByPesel(pesel);
    }
}
