package com.dudaj.cezspringapp.mapper;

import com.dudaj.cezspringapp.dto.NewReceiptDto;
import com.dudaj.cezspringapp.model.NewReceipt;

public class NewReceiptMapper {
    public static NewReceipt fromDto(NewReceiptDto newReceiptDto) {
        return new NewReceipt(
                newReceiptDto.pesel(),
                newReceiptDto.medicineName(),
                newReceiptDto.doseMilligrams()
        );
    }
}
