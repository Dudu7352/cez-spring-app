package com.dudaj.cezspringapp.controller.v1;

import com.dudaj.cezspringapp.dto.NewReceiptDto;
import com.dudaj.cezspringapp.dto.ReceiptDto;
import com.dudaj.cezspringapp.service.PatientService;
import com.dudaj.cezspringapp.service.ReceiptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReceiptController.class)
public class ReceiptControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private ReceiptService receiptService;

    @Test
    void deleteReceipt_shouldDelete() throws Exception {
        UUID uuid = UUID.fromString("4310ea1b-0854-463c-b064-abeb8f46aaad");

        mockMvc.perform(delete("/api/v1/receipts/{id}", uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(receiptService).removeReceipt(uuid);
    }

    @Test
    void postReceipt_shouldAddReceipt() throws Exception {
        String pesel = "80070881314";
        NewReceiptDto newReceiptDto = new NewReceiptDto(pesel, "ABC", 500d);
        UUID uuid = UUID.fromString("4310ea1b-0854-463c-b064-abeb8f46aaad");
        when(receiptService.addReceipt(any(NewReceiptDto.class))).thenReturn(
                new ReceiptDto(uuid, pesel, "ABCD", 550d)
        );

        mockMvc.perform(post("/api/v1/receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReceiptDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(uuid.toString()))
                .andExpect(jsonPath("$.pesel").value(pesel))
                .andExpect(jsonPath("$.medicineName").value("ABCD"))
                .andExpect(jsonPath("$.doseMilligrams").value(550d));

    }

    @Test
    void postReceipt_shouldReturnBadRequest_ifBodyIsInvalid() throws Exception {
        String pesel = "01234567890";
        NewReceiptDto newReceiptDto = new NewReceiptDto(pesel, "ABC", 500d);
        mockMvc.perform(post("/api/v1/receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReceiptDto)))
                .andExpect(status().isBadRequest());
    }
}
