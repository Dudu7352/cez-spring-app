package com.dudaj.cezspringapp.controller.v1;

import com.dudaj.cezspringapp.dto.PatientDto;
import com.dudaj.cezspringapp.dto.PrescriptionDto;
import com.dudaj.cezspringapp.exception.PatientAlreadyExistsException;
import com.dudaj.cezspringapp.exception.PatientNotFoundException;
import com.dudaj.cezspringapp.service.PatientService;
import com.dudaj.cezspringapp.service.PrescriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private PrescriptionService prescriptionService;

    @Test
    void getPatients_shouldReturnAllPatients() throws Exception {
        when(patientService.getAllPatients()).thenReturn(List.of(
                new PatientDto("80070881314", "Jan", "Kowalski"),
                new PatientDto("61102998734", "Anna", "Kowalska"))
        );

        mockMvc.perform(get("/api/v1/patients").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].pesel").value("80070881314"))
                .andExpect(jsonPath("$[0].name").value("Jan"))
                .andExpect(jsonPath("$[0].surname").value("Kowalski"))
                .andExpect(jsonPath("$[1].pesel").value("61102998734"))
                .andExpect(jsonPath("$[1].name").value("Anna"))
                .andExpect(jsonPath("$[1].surname").value("Kowalska"));
    }

    @Test
    void getPatientByPesel_shouldReturnPatient_whenPresent() throws Exception {
        String pesel = "80070881314";
        when(patientService.getPatient(pesel)).thenReturn(
                new PatientDto(pesel, "Jan", "Kowalski")
        );

        mockMvc.perform(get("/api/v1/patients/{pesel}", pesel).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pesel").value(pesel))
                .andExpect(jsonPath("$.name").value("Jan"))
                .andExpect(jsonPath("$.surname").value("Kowalski"));
    }

    @Test
    void getPatientByPesel_shouldNotFound_whenNotFound() throws Exception {
        String pesel = "80070881314";
        when(patientService.getPatient(pesel)).thenThrow(new PatientNotFoundException(""));

        mockMvc.perform(get("/api/v1/patients/{pesel}", pesel).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPatientByPesel_shouldReturnBadRequest_ifPeselIsInvalid() throws Exception {
        String pesel = "01234567890";

        mockMvc.perform(get("/api/v1/patients/{pesel}", pesel).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postPatient_shouldAddNewPatient() throws Exception {
        String pesel = "80070881314";
        when(patientService.addPatient(any(PatientDto.class))).thenReturn(
                new PatientDto(pesel, "Jan", "Kowalski")
        );
        PatientDto patientDto = new PatientDto(pesel, "Jan", "Kowalski");

        mockMvc.perform(
                        post("/api/v1/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pesel").value(pesel))
                .andExpect(jsonPath("$.name").value("Jan"))
                .andExpect(jsonPath("$.surname").value("Kowalski"));
    }

    @Test
    void postPatient_shouldReturnBadRequest_ifBodyIsInvalid() throws Exception {
        String invalidPesel = "01234567890";
        PatientDto patientDto = new PatientDto(invalidPesel, "", "");

        mockMvc.perform(
                        post("/api/v1/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postPatient_shouldConflict_onRedundantPost() throws Exception {
        when(patientService.addPatient(any(PatientDto.class))).thenThrow(new PatientAlreadyExistsException(""));
        PatientDto patientDto = new PatientDto("80070881314", "Jan2", "Kowalski2");

        mockMvc.perform(
                        post("/api/v1/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isConflict());
    }


    @Test
    void getPatientPrescriptionsByPesel_shouldReturnReceiverPrescriptions() throws Exception {
        UUID uuid1 = UUID.fromString("63248786-0fdb-4f22-9fe8-2df997686a7d");
        UUID uuid2 = UUID.fromString("4310ea1b-0854-463c-b064-abeb8f46aaad");
        String pesel = "80070881314";
        when(prescriptionService.getPatientsPrescriptions(pesel)).thenReturn(List.of(
                        new PrescriptionDto(uuid1, pesel, "ABC", 500),
                        new PrescriptionDto(uuid2, pesel, "XYZ", 600)
                )
        );

        mockMvc.perform(
                        get("/api/v1/patients/{pesel}/prescriptions", pesel)
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
    void getPatientPrescriptionsByPesel_shouldReturnBadRequest_ifPeselIsInvalid() throws Exception {
        String invalidPesel = "01234567890";
        mockMvc.perform(
                        get("/api/v1/patients/{pesel}/prescriptions", invalidPesel)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePatient_shouldDelete() throws Exception {
        String pesel = "80070881314";
        mockMvc.perform(
                delete("/api/v1/patients/{pesel}", pesel)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
        verify(patientService).removePatient(pesel);
    }

    @Test
    void deletePatient_shouldReturnBadRequest_idPeselIsInvalid() throws Exception {
        String invalidPesel = "01234567890";
        mockMvc.perform(
                delete("/api/v1/patients/{pesel}", invalidPesel)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
}