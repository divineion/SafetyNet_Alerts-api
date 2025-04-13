package com.safetynet.safetynetalertsapi.controllers;

import com.safetynet.safetynetalertsapi.constants.TestsConstants;
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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link FireStationController}.
 * This class tests all the endpoints related to fire stations, ensuring that each endpoint
 * behaves as expected by verifying the status code and the returned JSON data.
 */
@SpringBootTest
@AutoConfigureMockMvc //pour qur les objets mock soient disponibles
public class FireStationControllerTest {

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
     * Tests the GET endpoint <code>/firestation/{stationNumber}</code> to ensure it correctly
     * returns the list of persons covered by a given fire station.
     *
     * <p>Specifically, this test verifies that:
     * <ul>
     *     <li>The HTTP response has a status of <strong>200 OK</strong>.</li>
     *     <li>The <code>"covered persons"</code> field is a JSON array.</li>
     *     <li>Each person in the array contains the following fields:
     *         <ul>
     *             <li>firstName: a string</li>
     *             <li>lastName: a string</li>
     *             <li>address: a string</li>
     *             <li>phone: a phone number matching the <code>PHONE_PATTERN</code></li>
     *         </ul>
     *     </li>
     *     <li>The response also includes <code>children</code> and <code>adults</code> fields,
     *     which are numbers representing the count of children (18 or younger)
     *     and adults in the covered area.</li>
     * </ul>
     *
     */
    @Test
    public void testGetPersonsCoveredByStation() throws Exception {
        mockMvc.perform(get("/firestation/2"))
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$['covered persons']").isArray(),
                    jsonPath("$.['covered persons'][0].['firstName']").isString(),
                    jsonPath("$.['covered persons'][0].['lastName']").isString(),
                    jsonPath("$.['covered persons'][0].['address']").isString(),
                    jsonPath("$.['covered persons'][0].['phone']", matchesPattern(TestsConstants.PHONE_PATTERN)),
                    jsonPath("$.children").isNumber(),
                    jsonPath("$.adults").isNumber()
                );
    }

    /**
     * Test GET /phonealert/{stationNumber} to ensure it returns phone numbers of people served by the station.
     *
     */
    @Test
    public void testGetPhoneCoveredByStation() throws Exception {
        //échapper escape char
        mockMvc.perform(get("/phonealert/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]", matchesPattern(TestsConstants.PHONE_PATTERN)));
    }

    /**
     * Test GET /phonealert/{stationNumber} that should fail (the station number does not exist).
     *
     */
    @Test
    public void getCoveredPhoneShouldFailWithUnkownStationNumber() throws Exception {
        mockMvc.perform(get("/phonealert/555"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test GET /fire/{address} to return the list of residents at the given address.
     * The data should include:
     * <ul>
     *     <li>full name</li>
     *     <li>age,</li>
     *     <li>phone,</li>
     *     <li>allergies and medications</li>
     * </ul>
     */
    @Test
    public void testGetFireAlertInfoByAddress() throws Exception {
        mockMvc.perform(get("/fire/1509culverst"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").isNumber())
                .andExpect(jsonPath("$.residents").isArray())
                .andExpect(jsonPath("$.residents[0].age").isNumber())
                .andExpect(jsonPath("$.residents[0].allergies").isArray())
                .andExpect(jsonPath("$.residents[0].medications").isArray());
    }

    /**
     * Test GET /flood/{stations} to return flood alert info, including residents' personal details.
     */
    @Test
    public void testGetFloodAlertInfoByStations() throws Exception {
        mockMvc.perform(get("/flood/1,2,3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].address").exists())
                .andExpect(jsonPath("$[0].persons").isArray())
                .andExpect(jsonPath("$[0].persons[0].phone", matchesPattern(TestsConstants.PHONE_PATTERN)))
                .andExpect(jsonPath("$[0].persons[0].age").isNumber())
                .andExpect(jsonPath("$[0].persons[0].['allergies']").exists())
                .andExpect(jsonPath("$[0].persons[0].['medications']").exists())
        ;
    }

    /**
     * Test POST /firestation to create a new fire station.
     */
    @Test
    public void testCreateFireStation() throws Exception {
        String json = "{\"address\": \"Maple Street\", \"station\": 6}";
        mockMvc.perform(post("http://localhost:8080/firestation")
                        .content(json)
                        .contentType(TestsConstants.CONTENT_TYPE))
                .andExpect(jsonPath("$.station", is(6)));
    }

    /**
     * Test POST /firestation to create a new fire station that should fail (the station exists in the data source file).
     */
    @Test
    public void testCreateFireStationShouldReturnConflict() throws Exception {
        String json = "{\"address\": \"644 Gershwin Cir\", \"station\": 1}";
        mockMvc.perform(post("/firestation")
                        .contentType(TestsConstants.CONTENT_TYPE)
                        .content(json))
                .andExpect(status().isConflict());
    }

    /**
     * Test PUT /firestation/{address} to update an existing fire station.
     **/
    @Test
    public void testUpdateFireStation() throws Exception {
        String json = "{\"address\": \"29 15th St\", \"station\": 3}";

        mockMvc.perform(put("/firestation/2915thSt")
                .contentType("Application/json")
                .content(json))
                .andExpect(jsonPath("$.station", is(3)));
    }

    /**
     * Tests the update of a fire station that should fail due to a mismatch
     * between the provided address in the URL and the address in the request body.
     *
     * <p>This test verifies that:
     * <ul>
     *     <li>The URL contains the address <code>/firestation/1245Str</code>
     *     while the body contains the address <code>291555th St</code>.</li>
     *     <li>As the addresses do not match, the system should return
     *     a <strong>404 Not Found</strong> status.</li>
     * </ul>
     * </p>
     */
    @Test
    public void testUpdateFireStationShouldFailWithAddressMismatch() throws Exception {
        String json = "{\"address\": \"291555th St\", \"station\": 3}";

        mockMvc.perform(put("/firestation/1245Str")
                .contentType(TestsConstants.CONTENT_TYPE)
                .content(json))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the update of a fire station that should fail due to an
     * unknown provided address in the URL.
     */
    @Test
    public void testUpdateFireStationShouldFailWithUnkownAddress() throws Exception {
        String json = "{\"address\": \"29 15th St\", \"station\": 3}";
        mockMvc.perform(put("/firestation/2915thStsss"))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Test DELETE /firestation/{address} to delete a fire station.
     **/
    @Test void testDeleteFireStation() throws Exception {
        mockMvc.perform(delete("/firestation/1/644GershwinCir")
                .contentType(TestsConstants.CONTENT_TYPE))
                .andExpect(status().isNoContent());
    }

    /**
     * Test DELETE /firestation/{address} to delete a fire station.
     *
     */
    @Test void testDeleteFireStationShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/firestation/6/maplestreet")
                    .contentType(TestsConstants.CONTENT_TYPE)
                ).andExpect(status().isNotFound());
    }
}
