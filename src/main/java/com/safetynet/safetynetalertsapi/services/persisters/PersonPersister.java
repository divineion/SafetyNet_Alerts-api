package com.safetynet.safetynetalertsapi.services.persisters;

import com.safetynet.safetynetalertsapi.exceptions.PersonAlreadyExistsException;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.repositories.PersonRepository;
import com.safetynet.safetynetalertsapi.services.mappers.PersonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PersonPersister {

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper mapper;

    private final Logger logger = LogManager.getLogger(PersonPersister.class);

    public Person savePerson(Person person) {
        try {
            Person savedPerson = repository.save(person);

            return savedPerson;
        } catch (PersonAlreadyExistsException e) {
            logger.error(e.getMessage());
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
