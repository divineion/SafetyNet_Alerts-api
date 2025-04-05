package com.safetynet.safetynetalertsapi.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.safetynet.safetynetalertsapi.model.DataSet;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.dto.ChildDTO;
import com.safetynet.safetynetalertsapi.model.dto.PersonInfoDTO;
import com.safetynet.safetynetalertsapi.repositories.JsonDataProvider;
import com.safetynet.safetynetalertsapi.services.finders.PersonFinder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class PersonController {
	
	private static Logger logger = LogManager.getLogger(PersonController.class);
	
	@Autowired
	JsonDataProvider allDataProvider;
	
	@Autowired
	private PersonFinder personFinder;
	
	@GetMapping("/fetchAllData")
	public ResponseEntity<DataSet> getAllData() {
		DataSet data = allDataProvider.getAllData();
		
		return ResponseEntity.ok(data);
	}
	
	@GetMapping("/persons")
	public ResponseEntity<List<Person>> getAllPersons() throws JsonProcessingException {
			logger.debug("GET request received for /persons endpoint");
			
			List<Person> data = personFinder.findAll();
			
			logger.info("Data have been fetched successfully");
			return ResponseEntity.ok(data);		
	}
	
	@GetMapping("/personinfolastname/{lastName}")
	public ResponseEntity<List<PersonInfoDTO>> getPersonByLastName(@PathVariable String lastName) {
			logger.debug("Searching for persons named "+ lastName);
			List<PersonInfoDTO> data = personFinder.findBy(lastName);
			
			return ResponseEntity.ok(data);
	}
	
	@GetMapping("/communityemail/{city}")
	public ResponseEntity<List<String>> getAllEmailByCity(@PathVariable String city) {
		logger.debug("Searching for all email addresses in " + city);
		List<String> data = personFinder.findAllEmail(city);
		
		return ResponseEntity.ok(data);
	}
	
	@GetMapping("/childalert/{address}")
	public ResponseEntity<List<ChildDTO>> getAllChildrenByAddress(@PathVariable String address) {
		logger.debug("Searching for all children living at " + address);
		try {
			List<ChildDTO> data = personFinder.findAllChildrenByAddress(address.trim());
			return ResponseEntity.ok(data);
		} catch(Exception e) {
			logger.error("An error occurred while processing the request" + e);
		}
		return null;
	}
	
	public ResponseEntity<Person> createPerson(Person person) {
		return null;
	}
	
	public ResponseEntity<Person> updatePerson(Person person) {
		return null;
	}
	
	public ResponseEntity<Person> deletePerson(Person person) {
		return null;
	}
}