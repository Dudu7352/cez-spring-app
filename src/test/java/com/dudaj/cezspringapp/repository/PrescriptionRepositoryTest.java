package com.dudaj.cezspringapp.repository;

import com.dudaj.cezspringapp.model.NewPrescription;
import com.dudaj.cezspringapp.model.Prescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PrescriptionRepositoryTest {

    PrescriptionRepository prescriptionRepository;

    @BeforeEach
    void setUp() {
        prescriptionRepository = new PrescriptionRepository();
    }

    @Test
    void save_shouldReturnCreatedPrescription() {
        String pesel = "01234567890";
        NewPrescription newPrescription = new NewPrescription(pesel, "ABC", 500d);

        Prescription savedPrescription = prescriptionRepository.save(newPrescription);

        assertNotNull(savedPrescription.getId());
        assertEquals(pesel, savedPrescription.getPesel());
        assertEquals("ABC", savedPrescription.getMedicineName());
    }

    @Test
    void save_shouldCreateAndStoreNewPrescription() {
        String pesel = "01234567890";
        NewPrescription newPrescription = new NewPrescription(pesel, "ABC", 500d);

        prescriptionRepository.save(newPrescription);

        List<Prescription> prescriptions = prescriptionRepository.findByPesel(pesel);
        assertNotNull(prescriptions);
        assertEquals(1, prescriptions.size());
        Prescription savedPrescription = prescriptions.get(0);
        assertNotNull(savedPrescription.getId());
        assertEquals(pesel, savedPrescription.getPesel());
        assertEquals("ABC", savedPrescription.getMedicineName());
    }

    @Test
    void findByPesel_shouldReturnPrescriptions_whenExists() {
        String pesel = "01234567890";
        NewPrescription newPrescription1 = new NewPrescription(pesel, "ABC", 500d);
        NewPrescription newPrescription2 = new NewPrescription(pesel, "DEF", 500d);

        prescriptionRepository.save(newPrescription1);
        prescriptionRepository.save(newPrescription2);

        List<Prescription> results = prescriptionRepository.findByPesel(pesel);

        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    void findByPesel_shouldReturnNull_whenNotFound() {
        String pesel = "01234567890";

        List<Prescription> results = prescriptionRepository.findByPesel(pesel);

        assertTrue(results.isEmpty());
    }

    @Test
    void deleteById_shouldRemovePrescriptionFromList() {
        String pesel = "01234567890";
        NewPrescription newPrescription = mock(NewPrescription.class);
        when(newPrescription.getPesel()).thenReturn(pesel);

        prescriptionRepository.save(newPrescription);

        Prescription savedPrescription = prescriptionRepository.findByPesel(pesel).get(0);
        UUID generatedId = savedPrescription.getId();

        prescriptionRepository.deleteById(generatedId);

        List<Prescription> remainingPrescriptions = prescriptionRepository.findByPesel(pesel);
        assertTrue(remainingPrescriptions.isEmpty());
    }

    @Test
    void deleteByPesel_shouldRemoveAllPrescriptionsForPatient() {
        String pesel = "01234567890";
        NewPrescription newPrescription = mock(NewPrescription.class);
        when(newPrescription.getPesel()).thenReturn(pesel);

        prescriptionRepository.save(newPrescription);
        assertNotNull(prescriptionRepository.findByPesel(pesel));

        prescriptionRepository.deleteByPesel(pesel);

        assertTrue(prescriptionRepository.findByPesel(pesel).isEmpty());
    }
}