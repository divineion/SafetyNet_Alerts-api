package com.safetynet.safetynetalertsapi.controllers;

import com.safetynet.safetynetalertsapi.constants.TestsConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link PersonController}.
 * This class tests all the endpoints related to persons, ensuring that each endpoint
 * behaves as expected by verifying the status code and the returned JSON data.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {
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
     * Tests retrieving information about persons by last name.
     * Checks if the response contains expected person data :
     * <ul>
     *    <li>full name</li>
     *    <li>email</li>
     *    <li>age</li>
     *    <li>allergies and medications</li>
     * </ul>
     */
    @Test
    public void testGetPersonByLastName() throws Exception {
        mockMvc.perform(get("/personinfolastname/boyd"))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").isArray(),
            jsonPath("$.[0]").isMap(),
            jsonPath("$[0].age").isNumber(),
            jsonPath("$[0].lastName").isNotEmpty(),
            jsonPath("$[0].firstName").isNotEmpty(),
            jsonPath("$[0].email").isNotEmpty(),
            jsonPath("$[0].medications").isArray(),
            jsonPath("$[0].allergies").isArray()
        );
    }

    /**
     * Tests retrieving all emails by city.
     * Verifies if the response contains an array of emails.
     */
    @Test
    public void testGetAllEmailByCity() throws Exception {
        mockMvc.perform(get("/communityemail/culver"))
        .andExpectAll(
            status().isOk(),
            jsonPath("$").isArray(),
            jsonPath("$[0]", matchesPattern(TestsConstants.EMAIL_PATTERN))
        );
    }

    /**
     * Tests retrieving all children by address.
     * Verifies if the response contains children data with names, age, and household members.
     */
    @Test
    public void testGetAllChildrenByAddress() throws Exception {
        mockMvc.perform(get("/childalert/1509culverst"))
        .andExpectAll(
            status().isOk(),
            jsonPath("$").isArray(),
            jsonPath("$.[0].firstName").isString(),
            jsonPath("$.[0].lastName").isString(),
            jsonPath("$.[0].age").isNumber(),
            jsonPath("$.[0].houseHoldMembers").isArray()
        );
    }

    /**
     * Tests the creation of a new person and verifies the status and data.
     */
    @Test
    public void testCreatePerson() throws Exception {
        String json = "{\"firstName\": \"Helen\",\"lastName\": \"Burgess\",\"city\": \"Culver\",\"zip\": \"97451\",\"address\": \"Park St\",\"phone\": \"841-876-6253\",\"email\": \"random@imail.com\"}";
        mockMvc.perform(post("/person")
            .contentType(TestsConstants.CONTENT_TYPE)
            .content(json)
        ).andExpectAll(
            status().isCreated(),
            jsonPath("$.firstName").isNotEmpty(),
            jsonPath("$.lastName").isNotEmpty(),
            jsonPath("$.address").isNotEmpty(),
            jsonPath("$.city").isNotEmpty(),
            jsonPath("$.zip").isNotEmpty(),
            jsonPath("$.phone", matchesPattern(TestsConstants.PHONE_PATTERN)),
            jsonPath("$.email", matchesPattern(TestsConstants.EMAIL_PATTERN))
        );
    }

    /**
     * Tests the creation of a new person that should fail (the person exists already in the database).
     */
    @Test
    public void testCreatePersonShouldFailWithExistingPerson() throws Exception {
        String json = "{\"firstName\": \"Allison\",\"lastName\": \"Boyd\",\"address\": \"112 Steppes Pl\",\"city\": \"Culver\",\"zip\": \"97451\",\"phone\": \"841-874-9888\",\"email\": \"aly@imail.com\"}";
        mockMvc.perform(post("/person")
                    .contentType(TestsConstants.CONTENT_TYPE)
                    .content(json)
                ).andExpect(status().is4xxClientError());
    }

    /**
     * Tests updating a person's details and verifies successful update.
     */
    @Test
    public void testUpdatePerson() throws Exception {
        String json = "{\"firstName\": \"Allison\",\"lastName\": \"Boyd\",\"address\": \"112 Steppes Pl\",\"city\": \"Culver\",\"zip\": \"97451\",\"phone\": \"666-999-6666\",\"email\": \"aly@imail.com\"}";

        mockMvc.perform(put("/person/Boyd/Allison")
                    .contentType(TestsConstants.CONTENT_TYPE)
                    .content(json)
                ).andExpect(status().isOk());
    }

    /**
     * Tests updating a person with a mismatched identity (the url contains a name error), expecting failure.
     */
    @Test
    public void testUpdatePersonShouldFailWhenIdentityMismatch() throws Exception {
        String json = "{\"firstName\": \"John\", \"lastName\": \"Boyd\", \"address\": \"1509 Culver St\", \"city\": \"Culver\", \"zip\": \"97451\", \"phone\": \"00000-874-6512\",\"email\": \"jaboyd@email.com\"}";

        mockMvc.perform(put("/person/Boyde/john")
                    .contentType(TestsConstants.CONTENT_TYPE)
                    .content(json)
                ).andExpect(status().isUnprocessableEntity());
    }

    /**
     * Tests updating a person when that does not exist, expecting a failure.
     */
    @Test
    public void testUpdatePersonShouldFailWhenIdentityNotFound() throws Exception {
        String json = "{\"firstName\": \"Mickael\", \"lastName\": \"Boyd\", \"address\": \"1509 Culver St\", \"city\": \"Culver\", \"zip\": \"97451\", \"phone\": \"00000-874-6512\",\"email\": \"jaboyd@email.com\"}";

        mockMvc.perform(put("/person/boyd/mickael")
            .contentType(TestsConstants.CONTENT_TYPE)
            .content(json)
        ).andExpect(status().isNotFound());
    }

    /**
     * Tests the deletion of a person and verifies successful deletion.
     */
    @Test
    public void testDeletePerson() throws Exception {
        mockMvc.perform(delete("/person/boyd/john"))
            .andExpect(status().isNoContent());
    }

    /**
     * Tests attempting to delete a person that does not exist, expecting a failure.
     */
    @Test
    public void testDeletePersonShouldFail() throws Exception {
        mockMvc.perform(delete("/person/delley/helen"))
        .andExpect(status().isNotFound());
    }
}
