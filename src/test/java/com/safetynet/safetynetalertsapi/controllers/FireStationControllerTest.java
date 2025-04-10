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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc //pour qur les objets mock soient disponibles
public class FireStationControllerTest {

    private static final String CONTENT_TYPE = "Application/json";

    private static final String PHONE_PATTERN = "([\\d]{3}[-][\\d]{3}[-][\\d]{4})";


    @Autowired
    public MockMvc mockMvc;

    @BeforeEach
    public void prepareDataSource() throws IOException {
        Path source = Path.of("src/test/resources/database/data.json");
        Path target = Path.of("src/main/resources/database/data.json");
        Files.copy(source, target, REPLACE_EXISTING);
    }

    //TODO remove unused method before prod
    @Test
    public void testGetAllFireStations() throws Exception {
        //j'utilise l'attribut mockMvc et la méthode perform()
        // puis j'utilise la méthode qui correspond au verbe de ma requête
        //et je précise l'URI
        mockMvc.perform(get("/firestations"))
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$[0].station", is(1))
                );
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
     *             <li><code>firstName</code>: a string</li>
     *             <li><code>lastName</code>: a string</li>
     *             <li><code>address</code>: a string</li>
     *             <li><code>phone</code>: a phone number matching the <code>PHONE_PATTERN</code></li>
     *         </ul>
     *     </li>
     *     <li>The response also includes <code>children</code> and <code>adults</code> fields,
     *     which are numbers representing the count of children (18 or younger)
     *     and adults in the covered area.</li>
     * </ul>
     *
     * @throws Exception if an error occurs during the execution of the mock request.
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
                    jsonPath("$.['covered persons'][0].['phone']", matchesPattern(PHONE_PATTERN)),
                    jsonPath("$.children").isNumber(),
                    jsonPath("$.adults").isNumber()
                );
    }


    //Cette url doit retourner une liste des numéros de téléphone des résidents desservis
    //par la caserne de pompiers. Nous l'utiliserons pour envoyer des messages texte
    //d'urgence à des foyers spécifiques
    @Test
    public void testGetPhoneCoveredByStation() throws Exception {
        //échapper escape char
        mockMvc.perform(get("/phonealert/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]", matchesPattern(PHONE_PATTERN)));
    }

    @Test
    public void getCoveredPhoneShouldFailWithUnkownStationNumber() throws Exception {
        mockMvc.perform(get("/phonealert/555"))
                .andExpect(status().isNotFound());
    }

    //Cette url doit retourner la liste des habitants vivant à l’adresse donnée ainsi que le
    //numéro de la caserne de pompiers la desservant. La liste doit inclure le nom, le
    //numéro de téléphone, l'âge et les antécédents médicaux (médicaments, posologie et
    //allergies) de chaque personne.
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

    //Cette url doit retourner une liste de tous les foyers desservis par la caserne. Cette
    //liste doit regrouper les personnes par adresse. Elle doit aussi inclure le nom, le
    //numéro de téléphone et l'âge des habitants, et faire figurer leurs antécédents
    //médicaux (médicaments, posologie et allergies) à côté de chaque nom.
    @Test
    public void testGetFloodAlertInfoByStations() throws Exception {
        mockMvc.perform(get("/flood/1,2,3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].address").exists())
                .andExpect(jsonPath("$[0].persons").isArray())
                .andExpect(jsonPath("$[0].persons[0].phone", matchesPattern(PHONE_PATTERN)))
                .andExpect(jsonPath("$[0].persons[0].age").isNumber())
                .andExpect(jsonPath("$[0].persons[0].['allergies']").exists())
                .andExpect(jsonPath("$[0].persons[0].['medications']").exists())
        ;
    }

    @Test
    public void testCreateFireStation() throws Exception {
        String json = "{\"address\": \"Maple Street\", \"station\": 6}";
        mockMvc.perform(post("http://localhost:8080/firestation")
                        .content(json)
                        .contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.station", is(6)));
    }

    @Test
    public void testCreateFireStationShouldReturnConflict() throws Exception {
        String json = "{\"address\": \"644 Gershwin Cir\", \"station\": 1}";
        mockMvc.perform(post("/firestation")
                        .contentType(CONTENT_TYPE)
                        .content(json))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    public void testUpdateFireStation() throws Exception {
        String json = "{\"address\": \"29 15th St\", \"station\": 3}";

        mockMvc.perform(put("/firestation/2915thSt")
                .contentType("Application/json")
                .content(json))
                .andExpect(jsonPath("$.station", is(3)));
    }

    @Test
    public void testUpdateFireStationShouldFailWithAddressMismatch() throws Exception {
        String json = "{\"address\": \"291555th St\", \"station\": 3}";

        mockMvc.perform(put("/firestation/1245Str")
                .contentType(CONTENT_TYPE)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateFireStationShouldFailWithUnkownAddress() throws Exception {
        String json = "{\"address\": \"29 15th St\", \"station\": 3}";
        mockMvc.perform(put("/firestation/2915thStsss"))
                .andExpect(status().is4xxClientError());
    }

    @Test void testDeleteFireStation() throws Exception {
        mockMvc.perform(delete("/firestation/1/644GershwinCir")
                .contentType(CONTENT_TYPE))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test void testDeleteFireStationShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/firestation/6/maplestreet")
                        .contentType(CONTENT_TYPE))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
