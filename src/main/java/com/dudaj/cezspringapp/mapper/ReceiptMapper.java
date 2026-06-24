package com.dudaj.cezspringapp.mapper;

import com.dudaj.cezspringapp.dto.ReceiptDto;
import com.dudaj.cezspringapp.model.Receipt;

public class ReceiptMapper {
    public static ReceiptDto toDto(Receipt receipt) {
        return new ReceiptDto(
                receipt.getId(),
                receipt.getPesel(),
                receipt.getMedicineName(),
                receipt.getDoseMilligrams()
        );
    }

    public static Receipt fromDto(Receipt receipt) {
        return new Receipt(
                receipt.getId(),
                receipt.getPesel(),
                receipt.getMedicineName(),
                receipt.getDoseMilligrams()
        );
    }
}
