package com.safetynet.safetynetalertsapi.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordControllerTest {
    private static final String CONTENT_TYPE = "Application/json";

    @Autowired
    public MockMvc mockMvc;

    @BeforeEach
    public void prepareDataSource() throws IOException {
        Path source = Path.of("src/test/resources/database/data.json");
        Path target = Path.of("src/main/resources/database/data.json");
        Files.copy(source, target, REPLACE_EXISTING);
    }

    @Test
    public void testCreateMedicalRecord() throws Exception {
        String json = "{\"firstName\": \"Lola\",\"lastName\": \"Moore\",\"birthdate\": \"01/28/2000\",\"allergies\": [\"illisoxian\"],\"medications\": [\"thradox:700mg\"]}";
        mockMvc.perform(post("/medicalrecord")
                .contentType(CONTENT_TYPE)
                .content(json))
            .andExpect(status().isOk());
    }

    @Test
    public void testCreateMedicalRecordShouldReturnConflict() throws Exception {
        String json = "{\"firstName\": \"Lily\",\"lastName\": \"Cooper\",\"birthdate\": \"03/06/1994\",\"allergies\": [],\"medications\": []}";
        mockMvc.perform(post("/medicalrecord")
                        .contentType(CONTENT_TYPE)
                        .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdateMedicalRecord() throws Exception {
        String json = "{\"firstName\": \"Lily\",\"lastName\": \"Cooper\",\"birthdate\": \"03/06/1994\",\"allergies\": [\"illisoxian\"],\"medications\": []}";

        mockMvc.perform(put("/medicalrecord/cooper/lily")
                        .contentType(CONTENT_TYPE)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        mockMvc.perform(delete("/medicalrecord/cooper/lily")
                .contentType(CONTENT_TYPE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteMedicalRecordShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/medicalrecord/cooper/lola")
                .contentType(CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

}
