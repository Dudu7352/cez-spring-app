package com.dudaj.cezspringapp.controller.v1;

import com.dudaj.cezspringapp.dto.PatientDto;
import com.dudaj.cezspringapp.dto.ReceiptDto;
import com.dudaj.cezspringapp.service.PatientService;
import com.dudaj.cezspringapp.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {
    @Autowired
    private final PatientService patientService;
    @Autowired
    private final ReceiptService receiptService;

    @GetMapping
    public List<PatientDto> getPatients() {
        return patientService.getAllPatients();
    }

    @PostMapping
    public ResponseEntity<PatientDto> postPatient(@RequestBody PatientDto newPatient) {
        PatientDto addedPatient = patientService.addPatient(newPatient);
        return new ResponseEntity<>(addedPatient, HttpStatus.CREATED);
    }

    @GetMapping("/{patientPesel}")
    public PatientDto getPatientByPesel(@PathVariable String patientPesel) {
        return patientService.getPatient(patientPesel);
    }

    @GetMapping("/{patientPesel}/receipts")
    public List<ReceiptDto> getPatientReceiptsByPesel(@PathVariable String patientPesel) {
        return receiptService.getPatientsReceipts(patientPesel);
    }

    @DeleteMapping("/{patientPesel}")
    public ResponseEntity<Void> deletePatientByPesel(@PathVariable String patientPesel) {
        patientService.removePatient(patientPesel);
        return ResponseEntity.noContent().build();
    }
}
