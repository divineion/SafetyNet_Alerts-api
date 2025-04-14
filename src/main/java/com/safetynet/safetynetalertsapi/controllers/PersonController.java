package com.safetynet.safetynetalertsapi.controllers;

import java.util.List;

import com.safetynet.safetynetalertsapi.exceptions.IdentityMismatchException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.dto.PersonDTO;
import com.safetynet.safetynetalertsapi.services.persisters.PersonPersister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safetynet.safetynetalertsapi.model.DataSet;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.dto.ChildDTO;
import com.safetynet.safetynetalertsapi.model.dto.PersonInfoDTO;
import com.safetynet.safetynetalertsapi.repositories.JsonDataHandler;
import com.safetynet.safetynetalertsapi.services.finders.PersonFinder;

@RestController
public class PersonController {
	
	private final static Logger logger = LogManager.getLogger(PersonController.class);
	
	@Autowired
	JsonDataHandler dataHandler;
	
	@Autowired
	private PersonFinder personFinder;

	@Autowired
	private PersonPersister persister;
	
	@GetMapping("/personinfolastname/{lastName}")
	public ResponseEntity<List<PersonInfoDTO>> getPersonByLastName(@PathVariable String lastName) {
			List<PersonInfoDTO> data = personFinder.findByLastName(lastName);
			
			return ResponseEntity.ok(data);
	}
	
	@GetMapping("/communityemail/{city}")
	public ResponseEntity<List<String>> getAllEmailByCity(@PathVariable String city) {
		List<String> data = personFinder.findAllEmail(city);
		
		return ResponseEntity.ok(data);
	}
	
	@GetMapping("/childalert/{address}")
	public ResponseEntity<List<ChildDTO>> getAllChildrenByAddress(@PathVariable String address) {
		try {
			List<ChildDTO> data = personFinder.findAllChildrenByAddress(address.trim());
			return ResponseEntity.ok(data);
		} catch(Exception e) {
            logger.error("An error occurred while processing the request{}", e.getMessage());
			return null;
		}
	}

	/**
	 * Create - Add a new {@link Person}
	 * @param personDto An object {@link Person}
	 * @return The person object saved
	 */
	@PostMapping("/person")
	public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonDTO personDto) {
		try {
			PersonDTO savedPerson = persister.savePerson(personDto);
			return ResponseEntity.status(201).body(savedPerson);
		} catch(ResourceAlreadyExistsException e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
    }

	@PutMapping("/person/{lastName}/{firstName}")
	public ResponseEntity<PersonDTO> updatePerson(@PathVariable String lastName, @PathVariable String firstName, @RequestBody PersonDTO personDto) {
		try {
			PersonDTO updatedPersonDto = persister.updatePerson(personDto, lastName, firstName);
			return ResponseEntity.status(200).body(updatedPersonDto);
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
		String fullName = lastName + " " + firstName;
		try {
			persister.deletePerson(lastName, firstName);
			return ResponseEntity.noContent().build();
		} catch(ResourceNotFoundException e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(404).build();
		}
    }
}