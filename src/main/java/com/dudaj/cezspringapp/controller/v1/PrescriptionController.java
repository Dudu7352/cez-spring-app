package com.dudaj.cezspringapp.controller.v1;

import com.dudaj.cezspringapp.dto.NewPrescriptionDto;
import com.dudaj.cezspringapp.dto.PrescriptionDto;
import com.dudaj.cezspringapp.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {
    @Autowired
    private final PrescriptionService prescriptionService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescriptions(@PathVariable UUID id) {
        prescriptionService.removePrescription(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<PrescriptionDto> postPrescription(@RequestBody @Valid NewPrescriptionDto newPrescriptionDto) {
        PrescriptionDto addedPrescription = prescriptionService.addPrescription(newPrescriptionDto);
        return new ResponseEntity<>(addedPrescription, HttpStatus.CREATED);
    }
}
