package com.dudaj.cezspringapp.repository;

import com.dudaj.cezspringapp.helper.CollectionHelper;
import com.dudaj.cezspringapp.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PatientRepositoryTest {

    PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        patientRepository = new PatientRepository();
    }

    @Test
    void save_shouldPassSingleInsertion() {
        Patient patient = new Patient("01234567890", "Jan", "Kowalski");

        Optional<Patient> insertionResult = patientRepository.save(patient);

        assertTrue(insertionResult.isPresent());
    }

    @Test
    void save_shouldFailOnRepeatedPesel() {
        String pesel = "01234567890";
        Patient patient1 = new Patient(pesel, "Jan", "Kowalski");
        Patient patient2 = new Patient(pesel, "Pafnucy", "Grazel");

        patientRepository.save(patient1);
        Optional<Patient> insertionResult = patientRepository.save(patient2);

        assertTrue(insertionResult.isEmpty());
    }

    @Test
    void save_shouldReturnSavedPatient() {
        String pesel = "01234567890";
        Patient patient = new Patient(pesel, "Jan", "Kowalski");

        Optional<Patient> resultPatientOptional = patientRepository.save(patient);

        assertTrue(resultPatientOptional.isPresent());
        Patient resultPatient = resultPatientOptional.get();
        assertEquals(patient.getPesel(), resultPatient.getPesel());
        assertEquals(patient.getName(), resultPatient.getName());
        assertEquals(patient.getSurname(), resultPatient.getSurname());
    }

    @Test
    void findByPesel_shouldReturnPatient_whenExists() {
        String pesel = "01234567890";
        Patient patient = new Patient(pesel, "Jan", "Kowalski");
        patientRepository.save(patient);

        Optional<Patient> resultPatientOptional = patientRepository.findByPesel(pesel);

        assertTrue(resultPatientOptional.isPresent());
        Patient resultPatient = resultPatientOptional.get();
        assertEquals(patient.getPesel(), resultPatient.getPesel());
        assertEquals(patient.getName(), resultPatient.getName());
        assertEquals(patient.getSurname(), resultPatient.getSurname());
    }

    @Test
    void findByPesel_shouldReturnEmpty_whenNotFound() {
        String pesel = "01234567890";

        Optional<Patient> resultPatientOptional = patientRepository.findByPesel(pesel);

        assertTrue(resultPatientOptional.isEmpty());
    }


    @Test
    void deletePatientByPesel_shouldDeletePatient() {
        String pesel = "01234567890";
        Patient patient = new Patient(pesel, "Jan", "Kowalski");

        patientRepository.save(patient);
        patientRepository.deleteByPesel(pesel);

        assertTrue(patientRepository.findByPesel(pesel).isEmpty());
    }

    @Test
    void findByNameLike_shouldFindNamesLike() {
        Patient patient1 = new Patient("01234567890", "Jan", "Kowalski");
        Patient patient2 = new Patient("12345678901", "Mijanusz", "Kowalski");
        Patient patient3 = new Patient("23456789012", "Zbigniew", "Kowalski");
        patientRepository.save(patient1);
        patientRepository.save(patient2);
        patientRepository.save(patient3);

        LinkedList<Patient> results =  patientRepository.findByNameLike("jan");

        assertEquals(2, results.size());
        assertTrue(CollectionHelper.contains(results, (p) -> p.getPesel().equals(patient1.getPesel())));
        assertTrue(CollectionHelper.contains(results, (p) -> p.getPesel().equals(patient2.getPesel())));
    }

    @Test
    void findBySurnameLike_shouldFindSurnamesLike() {
        Patient patient1 = new Patient("01234567890", "Jan", "Kowalski");
        Patient patient2 = new Patient("12345678901", "Zbigniew", "Wolski");
        Patient patient3 = new Patient("23456789012", "Anna", "Kowalska");
        patientRepository.save(patient1);
        patientRepository.save(patient2);
        patientRepository.save(patient3);

        LinkedList<Patient> results =  patientRepository.findBySurnameLike("ski");

        assertEquals(2, results.size());
        assertTrue(CollectionHelper.contains(results, (p) -> p.getPesel().equals(patient1.getPesel())));
        assertTrue(CollectionHelper.contains(results, (p) -> p.getPesel().equals(patient2.getPesel())));
    }
}