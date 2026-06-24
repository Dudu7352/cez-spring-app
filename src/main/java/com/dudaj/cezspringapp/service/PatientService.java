package com.dudaj.cezspringapp.service;

import com.dudaj.cezspringapp.dto.PatientDto;
import com.dudaj.cezspringapp.exception.PatientAlreadyExistsException;
import com.dudaj.cezspringapp.exception.PatientNotFoundException;
import com.dudaj.cezspringapp.mapper.PatientMapper;
import com.dudaj.cezspringapp.model.Patient;
import com.dudaj.cezspringapp.repository.PatientRepository;
import com.dudaj.cezspringapp.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @return a patient with the specifier PESEL number
     * @throws PatientNotFoundException if patient was not found
     */
    public PatientDto getPatient(String pesel) {
        return patientRepository.findByPesel(pesel).map(PatientMapper::toDto)
                .orElseThrow(() -> new PatientNotFoundException("patient with pesel " + pesel + " was not found"));
    }

    /**
     * Returns all patients
     *
     * @return a list of all patients
     */
    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream().map(PatientMapper::toDto).toList();
    }

    /**
     * Adds a new patient.
     *
     * @param newPatientDto new patient
     * @return a new patient that has been added
     * @throws PatientAlreadyExistsException if patient has already been added
     */
    public PatientDto addPatient(PatientDto newPatientDto) {
        Patient newPatient = PatientMapper.fromDto(newPatientDto);
        return patientRepository
                .save(newPatient)
                .map(PatientMapper::toDto)
                .orElseThrow(
                        () -> new PatientAlreadyExistsException(
                                "patient with pesel " + newPatientDto.pesel() + "already exists"
                        )
                );
    }

    /**
     * Removes a patient and every receipt associated with his PESEL. This action is idempotent.
     *
     * @param pesel patient's PESEL
     */
    public void removePatient(String pesel) {
        patientRepository.deleteByPesel(pesel);
        receiptRepository.deleteByPesel(pesel);
    }
}
