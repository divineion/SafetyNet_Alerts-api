package com.safetynet.safetynetalertsapi.controllers;

import java.util.List;

import com.safetynet.safetynetalertsapi.services.persisters.PersonPersister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@GetMapping("/fetchAllData")
	public ResponseEntity<DataSet> getAllData() {
		DataSet data = dataHandler.getAllData();
		
		return ResponseEntity.ok(data);
	}
	
	@GetMapping("/persons")
	public ResponseEntity<List<Person>> getAllPersons() {
			logger.debug("GET request received for /persons endpoint");
			
			List<Person> data = personFinder.findAll();
			
			logger.info("Data have been fetched successfully");
			return ResponseEntity.ok(data);		
	}
	
	@GetMapping("/personinfolastname/{lastName}")
	public ResponseEntity<List<PersonInfoDTO>> getPersonByLastName(@PathVariable String lastName) {
        logger.debug("Searching for persons named {}", lastName);
			List<PersonInfoDTO> data = personFinder.findBy(lastName);
			
			return ResponseEntity.ok(data);
	}
	
	@GetMapping("/communityemail/{city}")
	public ResponseEntity<List<String>> getAllEmailByCity(@PathVariable String city) {
        logger.debug("Searching for all email addresses in {}", city);
		List<String> data = personFinder.findAllEmail(city);
		
		return ResponseEntity.ok(data);
	}
	
	@GetMapping("/childalert/{address}")
	public ResponseEntity<List<ChildDTO>> getAllChildrenByAddress(@PathVariable String address) {
        logger.debug("Searching for all children living at {}", address);
		try {
			List<ChildDTO> data = personFinder.findAllChildrenByAddress(address.trim());
			return ResponseEntity.ok(data);
		} catch(Exception e) {
            logger.error("An error occurred while processing the request{}", e.getMessage());
		}
		return null;
	}

	/**
	 * Create - Add a new {@link Person}
	 * @param person An object {@link Person}
	 * @return The person object saved
	 */
	@PostMapping("/person")
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
            Person savedperson = persister.savePerson(person);
			return ResponseEntity.status(201).body(savedperson);
    }
	
	public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
		return null;
	}
	
	public ResponseEntity<Person> deletePerson(@RequestBody Person person) {
		return null;
	}
}