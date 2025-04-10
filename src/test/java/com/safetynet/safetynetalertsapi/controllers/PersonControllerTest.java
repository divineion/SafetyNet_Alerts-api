package com.safetynet.safetynetalertsapi.controllers;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {
    private static final String CONTENT_TYPE = "Application/json";

    @Autowired
    public MockMvc mockMvc;

    @BeforeEach
    public void prepareDataSource() throws IOException {
        Path source = Path.of("src/test/resources/database/data.json");
        Path target = Path.of("src/main/resources/database/data.json");
        Files.copy(source, target, REPLACE_EXISTING);
    }

    //Cette url doit retourner le nom, l'adresse, l'âge, l'adresse mail et les antécédents
    //médicaux (médicaments, posologie et allergies) de chaque habitant. Si plusieurs
    //personnes portent le même nom, elles doivent toutes apparaître.
    @Test
    public void testGetPersonByLastName() throws Exception {
        mockMvc.perform(get("/personinfolastname/boyd"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").isMap())
                .andExpect(jsonPath("$[0].lastName").isNotEmpty())
                .andExpect(jsonPath("$[0].firstName").isNotEmpty())
                .andExpect(jsonPath("$[0].email").isNotEmpty())
                .andExpect(jsonPath("$[0].medications").isArray())
                .andExpect(jsonPath("$[0].allergies").isArray());
    }

    //Cette url doit retourner les adresses mail de tous les habitants de la ville.
    // TODO add regex
    @Test
    public void testGetAllEmailByCity() throws Exception {
        mockMvc.perform(get("/communityemail/culver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

//    Cette url doit retourner une liste d'enfants (tout individu âgé de 18 ans ou moins)
//    habitant à cette adresse. La liste doit comprendre le prénom et le nom de famille de
//    chaque enfant, son âge et une liste des autres membres du foyer. S'il n'y a pas
//    d'enfant, cette url peut renvoyer une chaîne vide.
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

    @Test
    public void testCreatePerson() throws Exception {
        String json = "{\"firstName\": \"Helen\",\"lastName\": \"Burgess\",\"city\": \"Culver\",\"zip\": \"97451\",\"address\": \"Park St\",\"phone\": \"841-876-6253\",\"email\": \"random@imail.com\"}";
        mockMvc.perform(post("/person")
                    .contentType(CONTENT_TYPE)
                    .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").isNotEmpty());
    }

    @Test
    public void testCreatePersonShouldFail() throws Exception {
        String json = "{\"firstName\": \"Allison\",\"lastName\": \"Boyd\",\"address\": \"112 Steppes Pl\",\"city\": \"Culver\",\"zip\": \"97451\",\"phone\": \"841-874-9888\",\"email\": \"aly@imail.com\"}";
        mockMvc.perform(post("/person")
                        .contentType(CONTENT_TYPE)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testUpdatePerson() throws Exception {
        String json = "{\"firstName\": \"Allison\",\"lastName\": \"Boyd\",\"address\": \"112 Steppes Pl\",\"city\": \"Culver\",\"zip\": \"97451\",\"phone\": \"666-999-6666\",\"email\": \"aly@imail.com\"}";

        mockMvc.perform(put("/person/Boyd/Allison")
                .contentType(CONTENT_TYPE)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdatePersonShouldFailWhenIdentityMismatch() throws Exception {
        String json = "{\"firstName\": \"John\", \"lastName\": \"Boyd\", \"address\": \"1509 Culver St\", \"city\": \"Culver\", \"zip\": \"97451\", \"phone\": \"00000-874-6512\",\"email\": \"jaboyd@email.com\"}";

        mockMvc.perform(put("/person/Boyde/john")
                    .contentType(CONTENT_TYPE)
                    .content(json)
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testUpdatePersonShouldFailWhenIdentityNotFound() throws Exception {
        String json = "{\"firstName\": \"Mickael\", \"lastName\": \"Boyd\", \"address\": \"1509 Culver St\", \"city\": \"Culver\", \"zip\": \"97451\", \"phone\": \"00000-874-6512\",\"email\": \"jaboyd@email.com\"}";

        mockMvc.perform(put("/person/boyd/mickael")
                .contentType(CONTENT_TYPE)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletePerson() throws Exception {
        mockMvc.perform(delete("/person/boyd/john"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testDeletePersonShouldFail() throws Exception {
        mockMvc.perform(delete("/person/burgess/helen"))
                .andExpect(status().isNotFound());
    }

}
