package com.dudaj.cezspringapp.repository;

import com.dudaj.cezspringapp.model.NewReceipt;
import com.dudaj.cezspringapp.model.Receipt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReceiptRepositoryTest {

    ReceiptRepository receiptRepository;

    @BeforeEach
    void setUp() {
        receiptRepository = new ReceiptRepository();
    }

    @Test
    void save_shouldCreateAndStoreNewReceipt() {
        String pesel = "01234567890";
        NewReceipt newReceipt = new NewReceipt(pesel, "ABC", 500d);

        receiptRepository.save(newReceipt);

        List<Receipt> receipts = receiptRepository.findByPesel(pesel);
        assertNotNull(receipts);
        assertEquals(1, receipts.size());
        Receipt savedReceipt = receipts.get(0);
        assertNotNull(savedReceipt.getId());
        assertEquals(pesel, savedReceipt.getPesel());
        assertEquals("ABC", savedReceipt.getMedicineName());
    }

    @Test
    void findByPesel_shouldReturnReceipts_whenExists() {
        String pesel = "01234567890";
        NewReceipt newReceipt1 = new NewReceipt(pesel, "ABC", 500d);
        NewReceipt newReceipt2 = new NewReceipt(pesel, "DEF", 500d);

        receiptRepository.save(newReceipt1);
        receiptRepository.save(newReceipt2);

        List<Receipt> results = receiptRepository.findByPesel(pesel);

        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    void findByPesel_shouldReturnNull_whenNotFound() {
        String pesel = "01234567890";

        List<Receipt> results = receiptRepository.findByPesel(pesel);

        assertNull(results);
    }

    @Test
    void deleteById_shouldRemoveReceiptFromList() {
        String pesel = "01234567890";
        NewReceipt newReceipt = mock(NewReceipt.class);
        when(newReceipt.getPesel()).thenReturn(pesel);

        receiptRepository.save(newReceipt);

        Receipt savedReceipt = receiptRepository.findByPesel(pesel).get(0);
        UUID generatedId = savedReceipt.getId();

        receiptRepository.deleteById(generatedId);

        List<Receipt> remainingReceipts = receiptRepository.findByPesel(pesel);
        assertTrue(remainingReceipts.isEmpty());
    }

    @Test
    void deleteByPesel_shouldRemoveAllReceiptsForPatient() {
        String pesel = "01234567890";
        NewReceipt newReceipt = mock(NewReceipt.class);
        when(newReceipt.getPesel()).thenReturn(pesel);

        receiptRepository.save(newReceipt);
        assertNotNull(receiptRepository.findByPesel(pesel));

        receiptRepository.deleteByPesel(pesel);

        assertNull(receiptRepository.findByPesel(pesel));
    }
}