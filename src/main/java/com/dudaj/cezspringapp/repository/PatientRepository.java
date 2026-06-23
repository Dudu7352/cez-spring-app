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

    public Optional<Patient> findByPesel(@Nonnull String pesel) {
        return Optional.ofNullable(peselToPatientMap.get(pesel));
    }

    public boolean save(@Nonnull Patient patient) {
        return peselToPatientMap.putIfAbsent(patient.getPesel(), patient) == null;
    }

    public void deleteByPesel(@Nonnull String pesel) {
        peselToPatientMap.remove(pesel);
    }
}
