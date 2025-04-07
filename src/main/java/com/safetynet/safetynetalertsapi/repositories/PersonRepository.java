package com.safetynet.safetynetalertsapi.repositories;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepository {

    public final Logger logger = LogManager.getLogger();
    @Autowired
    JsonDataHandler dataHandler;

    public Person save(Person person) throws ResourceAlreadyExistsException {
        List<Person> persons = dataHandler.findAllPersons();

        if ( persons.stream().anyMatch(p-> p.getIdentity().equals(person.getIdentity())) ) {
            throw new ResourceAlreadyExistsException(person.getIdentity().toString() + " is already in the database");
        }
        dataHandler.write(person);
        return person;
    }

    public Person update(Person person) throws ResourceNotFoundException {
        List<Person> persons = dataHandler.findAllPersons();

        if (persons.stream().noneMatch(p-> p.getIdentity().equals(person.getIdentity()))) {
            throw new ResourceNotFoundException(person.getIdentity().toString() + " is not found in the database");
        }
        dataHandler.update(person);
        return person;
    }

    public void delete(String identity) throws ResourceNotFoundException {
        List<Person> persons = dataHandler.findAllPersons();

        if (persons.stream().noneMatch(p -> p.getIdentity().toString().equals(identity))) {
            throw new ResourceNotFoundException(identity + " is not found in the database");
        }
        dataHandler.delete(Person.class, identity);
    }
}
