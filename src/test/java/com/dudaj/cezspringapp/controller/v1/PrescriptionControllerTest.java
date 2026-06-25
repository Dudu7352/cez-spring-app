package com.dudaj.cezspringapp.controller.v1;

import com.dudaj.cezspringapp.dto.NewPrescriptionDto;
import com.dudaj.cezspringapp.dto.PrescriptionDto;
import com.dudaj.cezspringapp.service.PatientService;
import com.dudaj.cezspringapp.service.PrescriptionService;
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

@WebMvcTest(PrescriptionController.class)
public class PrescriptionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private PrescriptionService prescriptionService;

    @Test
    void deletePrescription_shouldDelete() throws Exception {
        UUID uuid = UUID.fromString("4310ea1b-0854-463c-b064-abeb8f46aaad");

        mockMvc.perform(delete("/api/v1/prescriptions/{id}", uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(prescriptionService).removePrescription(uuid);
    }

    @Test
    void postPrescription_shouldAddPrescription() throws Exception {
        String pesel = "80070881314";
        NewPrescriptionDto newPrescriptionDto = new NewPrescriptionDto(pesel, "ABC", 500d);
        UUID uuid = UUID.fromString("4310ea1b-0854-463c-b064-abeb8f46aaad");
        when(prescriptionService.addPrescription(any(NewPrescriptionDto.class))).thenReturn(
                new PrescriptionDto(uuid, pesel, "ABCD", 550d)
        );

        mockMvc.perform(post("/api/v1/prescriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPrescriptionDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(uuid.toString()))
                .andExpect(jsonPath("$.pesel").value(pesel))
                .andExpect(jsonPath("$.medicineName").value("ABCD"))
                .andExpect(jsonPath("$.doseMilligrams").value(550d));

    }

    @Test
    void postPrescription_shouldReturnBadRequest_ifBodyIsInvalid() throws Exception {
        String pesel = "01234567890";
        NewPrescriptionDto newPrescriptionDto = new NewPrescriptionDto(pesel, "ABC", 500d);
        mockMvc.perform(post("/api/v1/prescriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPrescriptionDto)))
                .andExpect(status().isBadRequest());
    }
}
