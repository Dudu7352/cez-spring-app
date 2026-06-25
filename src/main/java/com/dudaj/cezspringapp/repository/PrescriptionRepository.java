package com.dudaj.cezspringapp.repository;

import com.dudaj.cezspringapp.model.NewPrescription;
import com.dudaj.cezspringapp.model.Prescription;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PrescriptionRepository {
    final Map<String, LinkedList<Prescription>> peselToPrescriptionsMap;
    final Map<UUID, String> idToPeselMap;

    PrescriptionRepository() {
        peselToPrescriptionsMap = new ConcurrentHashMap<>();
        idToPeselMap = new ConcurrentHashMap<>();
    }

    /**
     * Finds all prescriptions associated with the specified PESEL.
     *
     * @param pesel patient's PESEL number
     * @return a list of prescriptions associated with the specified PESEL. In none were found, an empty list is returned
     */
    public List<Prescription> findByPesel(String pesel) {
        List<Prescription> prescriptions = peselToPrescriptionsMap.get(pesel);
        if (prescriptions == null) {
            return new LinkedList<>();
        }
        return prescriptions;
    }

    /**
     * Saves a new prescription.
     *
     * @param newPrescription new prescription to insert
     * @return a newly inserted prescription object
     */
    public Prescription save(NewPrescription newPrescription) {
        List<Prescription> prescriptions = peselToPrescriptionsMap.computeIfAbsent(
                newPrescription.getPesel(),
                k -> new LinkedList<>()
        );
        Prescription prescription = new Prescription();

        prescription.setId(UUID.randomUUID());
        prescription.setPesel(newPrescription.getPesel());
        prescription.setMedicineName(newPrescription.getMedicineName());
        prescription.setDoseMilligrams(newPrescription.getDoseMilligrams());
        prescriptions.add(prescription);
        idToPeselMap.put(prescription.getId(), prescription.getPesel());

        return prescription;
    }

    /**
     * Deletes a prescription with the specified ID. This action is idempotent.
     *
     * @param id prescription's ID
     */
    public void deleteById(UUID id) {
        String pesel = idToPeselMap.get(id);
        if (pesel == null) {
            return;
        }
        List<Prescription> prescriptions = peselToPrescriptionsMap.get(pesel);
        prescriptions.removeIf(r -> r.getId().equals(id));
        idToPeselMap.remove(id);
    }

    /**
     * Deletes all prescriptions associated with the PESEL number.
     *
     * @param pesel patient's PESEL number
     */
    public void deleteByPesel(String pesel) {
        List<Prescription> prescriptions = peselToPrescriptionsMap.remove(pesel);
        for (Prescription prescription : prescriptions) {
            idToPeselMap.remove(prescription.getId());
        }
    }
}
