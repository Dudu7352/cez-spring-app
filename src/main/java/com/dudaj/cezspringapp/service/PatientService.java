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

    Optional<PatientDto> getPatient(String pesel) {
        return patientRepository.findByPesel(pesel).map(PatientMapper::toDto);
    }

    List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream().map(PatientMapper::toDto).toList();
    }

    Optional<PatientDto> addPatient(PatientDto newPatientDto) {
        Patient newPatient = PatientMapper.fromDto(newPatientDto);
        return patientRepository.save(newPatient).map(PatientMapper::toDto);
    }

    void removePatient(String pesel) {
        patientRepository.deleteByPesel(pesel);
        receiptRepository.deleteByPesel(pesel);
    }
}
