package com.dudaj.cezspringapp.controller.v1;

import com.dudaj.cezspringapp.dto.NewReceiptDto;
import com.dudaj.cezspringapp.dto.ReceiptDto;
import com.dudaj.cezspringapp.service.ReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/receipts")
@RequiredArgsConstructor
public class ReceiptController {
    @Autowired
    private final ReceiptService receiptService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceipt(@PathVariable UUID id) {
        receiptService.removeReceipt(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ReceiptDto> postReceipt(@RequestBody @Valid NewReceiptDto newReceiptDto) {
        ReceiptDto addedReceipt = receiptService.addReceipt(newReceiptDto);
        return new ResponseEntity<>(addedReceipt, HttpStatus.CREATED);
    }
}
