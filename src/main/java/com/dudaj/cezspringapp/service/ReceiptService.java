package com.dudaj.cezspringapp.service;

import com.dudaj.cezspringapp.dto.NewReceiptDto;
import com.dudaj.cezspringapp.dto.ReceiptDto;
import com.dudaj.cezspringapp.exception.PatientNotFoundException;
import com.dudaj.cezspringapp.mapper.NewReceiptMapper;
import com.dudaj.cezspringapp.mapper.ReceiptMapper;
import com.dudaj.cezspringapp.repository.PatientRepository;
import com.dudaj.cezspringapp.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    @Autowired
    private final PatientRepository patientRepository;
    @Autowired
    private final ReceiptRepository receiptRepository;

    /**
     * Returns a list of patient's receipts.
     *
     * @param pesel patient's pesel
     * @return a list of receipts associated with patient
     * @throws PatientNotFoundException if patient does not exist
     */
    public List<ReceiptDto> getPatientsReceipts(String pesel) {
        if (!patientRepository.patientExists(pesel)) {
            throw new PatientNotFoundException("patient with pesel " + pesel + " does not exist");
        }
        return receiptRepository.findByPesel(pesel).stream().map(ReceiptMapper::toDto).toList();
    }

    /**
     * Adds a new receipt.
     *
     * @param newReceiptDto new receipt
     * @return Added receipt
     */
    public ReceiptDto addReceipt(NewReceiptDto newReceiptDto) {
        return ReceiptMapper.toDto(receiptRepository.save(NewReceiptMapper.fromDto(newReceiptDto)));
    }

    /**
     * Removes a receipt with the specified id
     *
     * @param id receipt id
     */
    public void removeReceipt(UUID id) {
        receiptRepository.deleteById(id);
    }
}
