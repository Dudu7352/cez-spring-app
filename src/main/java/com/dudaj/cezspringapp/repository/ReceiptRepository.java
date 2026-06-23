package com.dudaj.cezspringapp.repository;

import com.dudaj.cezspringapp.model.NewReceipt;
import com.dudaj.cezspringapp.model.Receipt;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ReceiptRepository {
    final Map<String, LinkedList<Receipt>> peselToReceiptMap;
    final Map<UUID, String> idToPeselMap;

    ReceiptRepository() {
        peselToReceiptMap = new ConcurrentHashMap<>();
        idToPeselMap = new ConcurrentHashMap<>();
    }

    /**
     * Finds all receipts associated with the specified PESEL.
     *
     * @param pesel patient's PESEL number
     * @return a list of receipts associated with the specified PESEL. In none were found, an empty list is returned
     */
    public List<Receipt> findByPesel(String pesel) {
        List<Receipt> receipts = peselToReceiptMap.get(pesel);
        if(receipts == null) {
            return new LinkedList<>();
        }
        return receipts;
    }

    /**
     * Saves a new receipt.
     *
     * @param newReceipt new receipt to insert
     * @return a newly inserted receipt object
     */
    public Receipt save(NewReceipt newReceipt) {
        List<Receipt> receipts = peselToReceiptMap.computeIfAbsent(
                newReceipt.getPesel(),
                k -> new LinkedList<>()
        );
        Receipt receipt = new Receipt();

        receipt.setId(UUID.randomUUID());
        receipt.setPesel(newReceipt.getPesel());
        receipt.setMedicineName(newReceipt.getMedicineName());
        receipt.setDoseMilligrams(newReceipt.getDoseMilligrams());
        receipts.add(receipt);
        idToPeselMap.put(receipt.getId(), receipt.getPesel());

        return receipt;
    }

    /**
     * Deletes a receipt with the specified ID. This action is idempotent.
     *
     * @param id receipt's ID
     */
    public void deleteById(UUID id) {
        String pesel = idToPeselMap.get(id);
        if(pesel == null) {
            return;
        }
        List<Receipt> receipts = peselToReceiptMap.get(pesel);
        receipts.removeIf(r -> r.getId().equals(id));
        idToPeselMap.remove(id);
    }

    /**
     * Deletes all receipts associated with the PESEL number.
     *
     * @param pesel patient's PESEL number
     */
    public void deleteByPesel(String pesel) {
        List<Receipt> receipts = peselToReceiptMap.remove(pesel);
        for(Receipt receipt : receipts) {
            idToPeselMap.remove(receipt.getId());
        }
    }
}
