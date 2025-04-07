package com.safetynet.safetynetalertsapi.services.persisters;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.dto.PersonDTO;
import com.safetynet.safetynetalertsapi.repositories.PersonRepository;
import com.safetynet.safetynetalertsapi.services.mappers.PersonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonPersister {

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper mapper;

    private final Logger logger = LogManager.getLogger(PersonPersister.class);

    public PersonDTO savePerson(PersonDTO personDto) throws ResourceAlreadyExistsException {
        Person person = mapper.fromPersonDtoToPerson(personDto);
        Person savedPerson = repository.save(person);

        PersonDTO responsePerson = mapper.fromPersonToPersonDTO(savedPerson);
        return responsePerson;
    }

    public PersonDTO updatePerson(PersonDTO personDto) throws ResourceNotFoundException {
        Person person = mapper.fromPersonDtoToPerson(personDto);
        Person updatedPerson = repository.update(person);
        PersonDTO responsePerson = mapper.fromPersonToPersonDTO(updatedPerson);
            return responsePerson;
    }

    public void deletePerson(String identity) throws ResourceNotFoundException, RuntimeException {
        repository.delete(identity);
    }
}
