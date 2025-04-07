package com.safetynet.safetynetalertsapi.services.persisters;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.dto.PersonDTO;
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

    public PersonDTO savePerson(PersonDTO personDto) throws ResourceAlreadyExistsException {
        Person person = mapper.fromPersonDtoToPerson(personDto);
        try {
            Person savedPerson = repository.save(person);

            PersonDTO responsePerson = mapper.fromPersonToPersonDTO(savedPerson);
            return responsePerson;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PersonDTO updatePerson(PersonDTO personDto) throws ResourceNotFoundException {
        Person person = mapper.fromPersonDtoToPerson(personDto);
        try {
            Person updatedPerson = repository.update(person);

            PersonDTO responsePerson = mapper.fromPersonToPersonDTO(updatedPerson);
            return responsePerson;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
