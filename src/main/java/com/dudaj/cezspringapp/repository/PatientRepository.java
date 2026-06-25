package com.dudaj.cezspringapp.repository;

import com.dudaj.cezspringapp.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PatientRepository {

    final Map<String, Patient> peselToPatientMap;

    PatientRepository() {
        peselToPatientMap = new ConcurrentHashMap<>();
    }

    /**
     * Finds all saved patients
     *
     * @return a list of all patients
     */
    public List<Patient> findAll() {
        return peselToPatientMap.values().stream().toList();
    }

    /**
     * Checks if patient with the specified PESEL exists.
     *
     * @param pesel patient's PESEL
     * @return a boolean value set to true if the patient exists.
     */
    public boolean patientExists(String pesel) {
        return peselToPatientMap.containsKey(pesel);
    }

    /**
     * Finds a patient that has a specified PESEL. It is guaranteed that at most one patient has a single PESEL.
     *
     * @param pesel patient's PESEL number
     * @return a patient with the specified PESEL wrapped inside Optional container. If no patient was found, Optional.empty() is returned
     */
    public Optional<Patient> findByPesel(String pesel) {
        return Optional.ofNullable(peselToPatientMap.get(pesel));
    }

    /**
     * Saves a new patient
     *
     * @param patient new patient to be saved
     * @return on success a newly inserted patient object is returned wrapped inside Optional container. Otherwise, Optional.empty() is returned
     */
    public Optional<Patient> save(Patient patient) {
        if (peselToPatientMap.containsKey(patient.getPesel())) {
            return Optional.empty();
        }
        peselToPatientMap.put(patient.getPesel(), patient);
        return Optional.of(patient);
    }

    /**
     * Deletes a patient with the specified PESEL. This action is idempotent.
     *
     * @param pesel patient's PESEL number
     */
    public void deleteByPesel(String pesel) {
        peselToPatientMap.remove(pesel);
    }
}
