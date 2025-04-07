package com.safetynet.safetynetalertsapi.repositories;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class PersonRepository {

    public final Logger logger = LogManager.getLogger();
    @Autowired
    JsonDataHandler dataHandler;

    public Person save(Person person) throws ResourceAlreadyExistsException, IOException {
        List<Person> persons = dataHandler.findAllPersons();

        if ( persons.stream().anyMatch(p-> p.getIdentity().equals(person.getIdentity())) ) {
            throw new ResourceAlreadyExistsException(person.getIdentity().toString() + " is already in the database");
        }

        dataHandler.write(person);

        return person;
    }
}
