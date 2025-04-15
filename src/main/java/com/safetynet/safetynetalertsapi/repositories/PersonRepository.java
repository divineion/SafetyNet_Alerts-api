package com.safetynet.safetynetalertsapi.repositories;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepository implements BaseRepository<Person> {

    @Autowired
    JsonDataHandler dataHandler;

    @Autowired
    StringFormatter formatter;

    /**
     * Retrieves all persons in the data source file.
     *
     * @return a list of all {@link Person} instances
     */
    @Override
    public List<Person> findAll() {
        return dataHandler.getAllData().getPersons();
    }

    public List<Person> findByLastName(String lastName) throws ResourceNotFoundException {
        if (findAll().stream()
                .noneMatch(
                        person -> StringFormatter.normalizeString(person.getIdentity().getLastName())
                                .equals(StringFormatter.normalizeString(lastName)) )) {
            throw new ResourceNotFoundException("The provided lastname is not found");
        }
        return findAll().stream().filter(p -> p.getIdentity().getLastName().equalsIgnoreCase(lastName)).toList();
    }

    public Person save(Person person) throws ResourceAlreadyExistsException {
        List<Person> persons = findAll();

        if ( persons.stream().anyMatch(p-> p.getIdentity().equals(person.getIdentity())) ) {
            throw new ResourceAlreadyExistsException(person.getIdentity().toString() + " is already in the database");
        }
        dataHandler.write(person);
        return person;
    }

    public Person update(Person person) throws ResourceNotFoundException {
        List<Person> persons = findAll();

        if (persons.stream().noneMatch(p-> p.getIdentity().equals(person.getIdentity()))) {
            throw new ResourceNotFoundException(person.getIdentity().toString() + " is not found in the database");
        }
        dataHandler.update(person);
        return person;
    }

    public void delete(String lastName, String firstName) throws ResourceNotFoundException, RuntimeException {
        List<Person> persons = findAll();
        String uniqueIdentifier = formatter.normalizeString(firstName.concat(lastName));

        Person personToDelete = persons
                .stream()
                .filter(p -> formatter.normalizeString(p.getIdentity().toString()).equals(uniqueIdentifier))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(uniqueIdentifier + " is not found in the database"));

        dataHandler.delete(Person.class, personToDelete);
    }
}
