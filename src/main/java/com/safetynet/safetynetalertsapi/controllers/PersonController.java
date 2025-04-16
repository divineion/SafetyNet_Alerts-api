package com.safetynet.safetynetalertsapi.controllers;

import com.safetynet.safetynetalertsapi.exceptions.IdentityMismatchException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.Identifiable;
import com.safetynet.safetynetalertsapi.model.Identity;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.dto.ChildDTO;
import com.safetynet.safetynetalertsapi.model.dto.PersonDTO;
import com.safetynet.safetynetalertsapi.model.dto.PersonInfoDTO;
import com.safetynet.safetynetalertsapi.repositories.JsonDataHandler;
import com.safetynet.safetynetalertsapi.services.finders.PersonFinder;
import com.safetynet.safetynetalertsapi.services.persisters.PersonPersister;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class PersonController {
	
	private final static Logger logger = LogManager.getLogger(PersonController.class);
	
	@Autowired
	JsonDataHandler dataHandler;
	
	@Autowired
	private PersonFinder personFinder;

	@Autowired
	private PersonPersister persister;

	@Autowired
	private Validator jakartaValidator;

	private <T extends Identifiable> void validatePerson(T object) throws ConstraintViolationException {
		Set <ConstraintViolation<Identity>> violations = jakartaValidator.validate(object.getIdentity());
			if(!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}
	
	@GetMapping("/personinfolastname/{lastName}")
	public ResponseEntity<List<PersonInfoDTO>> getPersonByLastName(@PathVariable String lastName) {
        try {
            List<PersonInfoDTO> data = personFinder.findByLastName(lastName);
            return ResponseEntity.ok(data);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) ;
        }
    }
	
	@GetMapping("/communityemail/{city}")
	public ResponseEntity<List<String>> getAllEmailByCity(@PathVariable String city) {
        List<String> data = null;
        try {
            data = personFinder.findEmailListByCity(city);
			return ResponseEntity.ok(data);
        } catch (ResourceNotFoundException e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	@GetMapping("/childalert/{address}")
	public ResponseEntity<List<ChildDTO>> getAllChildrenByAddress(@PathVariable String address) {
		try {
			List<ChildDTO> data = personFinder.findAllChildrenByAddress(address.trim());
			return ResponseEntity.ok(data);
		} catch(ResourceNotFoundException e) {
            logger.error("An error occurred while processing the request{}", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	/**
	 * Create - Add a new {@link Person}
	 * @param personDto An object {@link Person}
	 * @return The person object saved
	 */
	@PostMapping("/person")
	public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonDTO personDto) {
		try {
			validatePerson(personDto);
			PersonDTO savedPerson = persister.savePerson(personDto);
			return ResponseEntity.status(201).body(savedPerson);
		} catch (ConstraintViolationException e){
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch(ResourceAlreadyExistsException e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
    }

	@PutMapping("/person/{lastName}/{firstName}")
	public ResponseEntity<PersonDTO> updatePerson( @Valid @PathVariable String lastName, @PathVariable String firstName, @RequestBody PersonDTO personDto) {
		try {
			validatePerson(personDto);
			PersonDTO updatedPersonDto = persister.updatePerson(personDto, lastName, firstName);
			return ResponseEntity.status(200).body(updatedPersonDto);
		}catch(ConstraintViolationException e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch(ResourceNotFoundException e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(404).build();
		} catch (IdentityMismatchException e) {
           logger.error(e.getMessage());
		   return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

	@DeleteMapping("/person/{lastName}/{firstName}")
	public HttpEntity<?> deletePerson(@PathVariable String lastName, @PathVariable String firstName) {
		try {
			persister.deletePerson(lastName, firstName);
			return ResponseEntity.noContent().build();
		} catch(ResourceNotFoundException e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(404).build();
		}
    }
}