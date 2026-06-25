package com.dudaj.cezspringapp.service;

import com.dudaj.cezspringapp.dto.NewPrescriptionDto;
import com.dudaj.cezspringapp.dto.PrescriptionDto;
import com.dudaj.cezspringapp.exception.PatientNotFoundException;
import com.dudaj.cezspringapp.mapper.NewPrescriptionMapper;
import com.dudaj.cezspringapp.mapper.PrescriptionMapper;
import com.dudaj.cezspringapp.repository.PatientRepository;
import com.dudaj.cezspringapp.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrescriptionService {
    @Autowired
    private final PatientRepository patientRepository;
    @Autowired
    private final PrescriptionRepository prescriptionRepository;

    /**
     * Returns a list of patient's prescriptions.
     *
     * @param pesel patient's pesel
     * @return a list of prescriptions associated with patient
     * @throws PatientNotFoundException if patient does not exist
     */
    public List<PrescriptionDto> getPatientsPrescriptions(String pesel) {
        if (!patientRepository.patientExists(pesel)) {
            throw new PatientNotFoundException("patient with pesel " + pesel + " does not exist");
        }
        return prescriptionRepository.findByPesel(pesel).stream().map(PrescriptionMapper::toDto).toList();
    }

    /**
     * Adds a new prescription.
     *
     * @param newPrescriptionDto new prescription
     * @return Added prescription
     */
    public PrescriptionDto addPrescription(NewPrescriptionDto newPrescriptionDto) {
        return PrescriptionMapper.toDto(prescriptionRepository.save(NewPrescriptionMapper.fromDto(newPrescriptionDto)));
    }

    /**
     * Removes a prescription with the specified id
     *
     * @param id prescription id
     */
    public void removePrescription(UUID id) {
        prescriptionRepository.deleteById(id);
    }
}
