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

/**
 * Test class for the {@link MedicalRecordController}.
 * This class tests all the endpoints related to medical records, ensuring that each endpoint
 * behaves as expected by verifying the status code and the returned JSON data.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordControllerTest {
    private static final String CONTENT_TYPE = "Application/json";

    @Autowired
    public MockMvc mockMvc;

    /**
     * Sets up the test data by copying a sample JSON file into the main resources directory.
     * This ensures that the test data is available for the test cases.
     *
     * @throws IOException if the test data file cannot be copied.
     */
    @BeforeEach
    public void prepareDataSource() throws IOException {
        Path source = Path.of("src/test/resources/database/data.json");
        Path target = Path.of("src/main/resources/database/data.json");
        Files.copy(source, target, REPLACE_EXISTING);
    }

    /**
     * Tests creating a medical record with valid data.
     */
    @Test
    public void testCreateMedicalRecord() throws Exception {
        String json = "{\"firstName\": \"Lola\",\"lastName\": \"Moore\",\"birthdate\": \"01/28/2000\",\"allergies\": [\"illisoxian\"],\"medications\": [\"thradox:700mg\"]}";
        mockMvc.perform(post("/medicalrecord")
                    .contentType(CONTENT_TYPE)
                    .content(json)
                ).andExpect(status().isOk());
    }

    /**
     * Tests creating a medical record with conflicting data (a medical record with these firstname/lastname exists already in the database).
     */
    @Test
    public void testCreateMedicalRecordShouldReturnConflict() throws Exception {
        String json = "{\"firstName\": \"Lily\",\"lastName\": \"Cooper\",\"birthdate\": \"03/06/1994\",\"allergies\": [],\"medications\": []}";
        mockMvc.perform(post("/medicalrecord")
                    .contentType(CONTENT_TYPE)
                    .content(json)
                ).andExpect(status().isConflict());
    }

    /**
     * Tests updating a medical record with valid data.
     */
    @Test
    public void testUpdateMedicalRecord() throws Exception {
        String json = "{\"firstName\": \"Tenley\",\"lastName\": \"Boyd\",\"birthdate\": \"03/06/1994\",\"allergies\": [\"illisoxian\"],\"medications\": []}";

        mockMvc.perform(put("/medicalrecord/boyd/tenley")
                    .contentType(CONTENT_TYPE)
                    .content(json)
                ).andExpect(status().isOk());
    }

    /**
     * Tests updating a medical record with a mismatched identity
     * (the url doesn't match the firstname/lastname properties).
     */
    @Test
    public void testUpdateMedicalRecordShouldFailWithIdentityMismatch() throws Exception {
        String json = "{\"firstName\": \"Lily\",\"lastName\": \"Cooper\",\"birthdate\": \"03/06/1994\",\"allergies\": [\"illisoxian\"],\"medications\": []}";

        mockMvc.perform(put("//medicalrecord/moore/billy")
                    .contentType(CONTENT_TYPE)
                    .content(json)
                ).andExpect(status().is4xxClientError());
    }

    /**
     * Tests updating a medical record that matches no identity in the database.
     */
    @Test
    public void testUpdateMedicalRecordShouldFailWithUnknownIdentity() throws Exception {
        String json = "{\"firstName\": \"Lola\",\"lastName\": \"Moore\",\"birthdate\": \"03/06/1994\",\"allergies\": [\"illisoxian\"],\"medications\": []}";

        mockMvc.perform(put("//medicalrecord/moore/lola")
                    .contentType(CONTENT_TYPE)
                    .content(json)
                ).andExpect(status().isNotFound());
    }

    /**
     * Tests deleting a medical record with a valid identity.
     */
    @Test
    public void testDeleteMedicalRecord() throws Exception {
        mockMvc.perform(delete("/medicalrecord/cooper/lily")
                    .contentType(CONTENT_TYPE)
                ).andExpect(status().isNoContent());
    }

    /**
     * Tests deleting a medical record that matches no identity in the database.
     */
    @Test
    public void testDeleteMedicalRecordShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/medicalrecord/cooper/lola")
                    .contentType(CONTENT_TYPE)
                ).andExpect(status().isNotFound());
    }
}
