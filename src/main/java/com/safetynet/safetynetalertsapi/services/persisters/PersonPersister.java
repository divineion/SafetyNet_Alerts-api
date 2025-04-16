package com.safetynet.safetynetalertsapi.services.persisters;

import com.safetynet.safetynetalertsapi.exceptions.IdentityMismatchException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.dto.PersonDTO;
import com.safetynet.safetynetalertsapi.repositories.PersonRepository;
import com.safetynet.safetynetalertsapi.services.mappers.PersonMapper;
import com.safetynet.safetynetalertsapi.services.validators.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonPersister {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonMapper mapper;

    @Autowired
    private PersonValidator validator;

    public PersonDTO savePerson(PersonDTO personDto) throws ResourceAlreadyExistsException {
        Person person = mapper.fromPersonDtoToPerson(personDto);
        Person savedPerson = repository.save(person);

        PersonDTO responsePerson = mapper.fromPersonToPersonDTO(savedPerson);
        return responsePerson;
    }

    public PersonDTO updatePerson(PersonDTO personDto, String lastName, String firstName) throws ResourceNotFoundException, IdentityMismatchException {
        validator.validateIdentityMatches(personDto, lastName, firstName);

        Person person = mapper.fromPersonDtoToPerson(personDto);
        Person updatedPerson = repository.update(person);
        PersonDTO responsePerson = mapper.fromPersonToPersonDTO(updatedPerson);
            return responsePerson;
    }

    public void deletePerson(String lastName, String firstName) throws ResourceNotFoundException, RuntimeException {
        repository.delete(lastName, firstName);
    }
}
