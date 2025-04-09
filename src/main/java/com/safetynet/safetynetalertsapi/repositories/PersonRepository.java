package com.safetynet.safetynetalertsapi.repositories;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepository implements BaseRepository<Person> {

    public final Logger logger = LogManager.getLogger();
    @Autowired
    JsonDataHandler dataHandler;

    @Autowired
    StringFormatter formatter;

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

    public void delete(String lastName, String firstName) throws ResourceNotFoundException, RuntimeException {
        List<Person> persons = dataHandler.findAllPersons();
        String uniqueIdentifier = formatter.normalizeString(firstName.concat(lastName));

        Person personToDelete = persons
                .stream()
                .filter(p -> formatter.normalizeString(p.getIdentity().toString()).equals(uniqueIdentifier))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(uniqueIdentifier + " is not found in the database"));

        dataHandler.delete(Person.class, personToDelete);
    }
}
