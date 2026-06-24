package com.dudaj.cezspringapp.controller.v1;

import com.dudaj.cezspringapp.dto.PatientDto;
import com.dudaj.cezspringapp.dto.ReceiptDto;
import com.dudaj.cezspringapp.exception.PatientAlreadyExistsException;
import com.dudaj.cezspringapp.exception.PatientNotFoundException;
import com.dudaj.cezspringapp.service.PatientService;
import com.dudaj.cezspringapp.service.ReceiptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
@AutoConfigureRestTestClient
class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private ReceiptService receiptService;

    @Test
    void getPatients_shouldReturnAllPatients() throws Exception {
        when(patientService.getAllPatients()).thenReturn(List.of(
                new PatientDto("01234567890", "Jan", "Kowalski"),
                new PatientDto("12345678900", "Anna", "Kowalska"))
        );

        mockMvc.perform(get("/api/v1/patients").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].pesel").value("01234567890"))
                .andExpect(jsonPath("$[0].name").value("Jan"))
                .andExpect(jsonPath("$[0].surname").value("Kowalski"))
                .andExpect(jsonPath("$[1].pesel").value("12345678900"))
                .andExpect(jsonPath("$[1].name").value("Anna"))
                .andExpect(jsonPath("$[1].surname").value("Kowalska"));
    }

    @Test
    void getPatientByPesel_shouldReturnPatient_whenPresent() throws Exception {
        String pesel = "01234567890";
        when(patientService.getPatient(pesel)).thenReturn(
                new PatientDto(pesel, "Jan", "Kowalski")
        );

        mockMvc.perform(get("/api/v1/patients/{pesel}", pesel).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pesel").value("01234567890"))
                .andExpect(jsonPath("$.name").value("Jan"))
                .andExpect(jsonPath("$.surname").value("Kowalski"));
    }

    @Test
    void getPatientByPesel_shouldNotFound_whenNotFound() throws Exception {
        String pesel = "01234567890";
        when(patientService.getPatient(pesel)).thenThrow(new PatientNotFoundException(""));

        mockMvc.perform(get("/api/v1/patients/{pesel}", pesel).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void postPatient_shouldAddNewPatient() throws Exception {
        when(patientService.addPatient(any(PatientDto.class))).thenReturn(
                new PatientDto("01234567890", "Jan", "Kowalski")
        );
        PatientDto patientDto = new PatientDto("12345678900", "Jan2", "Kowalski2");

        mockMvc.perform(
                        post("/api/v1/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pesel").value("01234567890"))
                .andExpect(jsonPath("$.name").value("Jan"))
                .andExpect(jsonPath("$.surname").value("Kowalski"));
    }

    @Test
    void postPatient_shouldConflict_onRedundantPost() throws Exception {
        when(patientService.addPatient(any(PatientDto.class))).thenThrow(new PatientAlreadyExistsException(""));
        PatientDto patientDto = new PatientDto("12345678900", "Jan2", "Kowalski2");

        mockMvc.perform(
                        post("/api/v1/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isConflict());
    }


    @Test
    void getPatientReceiptsByPesel_shouldReturnReceiverReceipts() throws Exception {
        UUID uuid1 = UUID.fromString("63248786-0fdb-4f22-9fe8-2df997686a7d");
        UUID uuid2 = UUID.fromString("4310ea1b-0854-463c-b064-abeb8f46aaad");
        String pesel = "0123456789";
        when(receiptService.getPatientsReceipts(pesel)).thenReturn(List.of(
                        new ReceiptDto(uuid1, pesel, "ABC", 500),
                        new ReceiptDto(uuid2, pesel, "XYZ", 600)
                )
        );

        mockMvc.perform(
                        get("/api/v1/patients/{pesel}/receipts", pesel)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(uuid1.toString()))
                .andExpect(jsonPath("$[0].pesel").value(pesel))
                .andExpect(jsonPath("$[0].medicineName").value("ABC"))
                .andExpect(jsonPath("$[0].doseMilligrams").value(500))
                .andExpect(jsonPath("$[1].id").value(uuid2.toString()))
                .andExpect(jsonPath("$[1].pesel").value(pesel))
                .andExpect(jsonPath("$[1].medicineName").value("XYZ"))
                .andExpect(jsonPath("$[1].doseMilligrams").value(600));
    }

    @Test
    void deletePatient_shouldDelete() throws Exception {
        mockMvc.perform(
                delete("/api/v1/patients/01234567890")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }
}