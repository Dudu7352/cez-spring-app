package com.dudaj.cezspringapp.repository;

import com.dudaj.cezspringapp.model.Patient;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PatientRepository {

    Map<String, Patient> peselToPatientMap;

    PatientRepository() {
        peselToPatientMap = new ConcurrentHashMap<>();
    }

    /**
     * finds all patients which names contain a specified name fragment.
     * Checking algorithm is case-insensitive and uses default spring locale.
     *
     * @param name search query
     * @return a list of found patients that match the query
     */
    public LinkedList<Patient> findByNameLike(@Nonnull String name) {
        LinkedList<Patient> result = new LinkedList<>();
        Locale locale = LocaleContextHolder.getLocale();
        name = name.toLowerCase(locale);
        for(Patient p: peselToPatientMap.values()) {
            if(p.getName().toLowerCase(locale).contains(name)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Finds all patients which surnames contain a specified surname fragment.
     * Checking algorithm is case-insensitive and uses default spring locale.
     *
     * @param surname search query
     * @return a list of found patients that match the query. If none were found, an empty list is returned
     */
    public LinkedList<Patient> findBySurnameLike(@Nonnull String surname) {
        LinkedList<Patient> result = new LinkedList<>();
        Locale locale = LocaleContextHolder.getLocale();
        surname = surname.toLowerCase(locale);
        for(Patient p: peselToPatientMap.values()) {
            if(p.getSurname().toLowerCase(locale).contains(surname)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Finds a patient that has a specified PESEL. It is guaranteed that at most one patient has a single PESEL.
     *
     * @param pesel patient's PESEL number
     * @return a patient with the specified PESEL wrapped inside Optional container. If no patient was found, Optional.empty() is returned
     */
    public Optional<Patient> findByPesel(@Nonnull String pesel) {
        return Optional.ofNullable(peselToPatientMap.get(pesel));
    }

    /**
     * Saves a new patient
     *
     * @param patient new patient to be saved
     * @return on success a newly inserted patient object is returned wrapped inside Optional container. Otherwise, Optional.empty() is returned
     */
    public Optional<Patient> save(@Nonnull Patient patient) {
        if(peselToPatientMap.containsKey(patient.getPesel())) {
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
    public void deleteByPesel(@Nonnull String pesel) {
        peselToPatientMap.remove(pesel);
    }
}
