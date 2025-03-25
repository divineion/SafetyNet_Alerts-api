package com.safetynet.safetynetalertsapi.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.safetynetalertsapi.services.JsonDataFinder;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class PersonController {
	
	@Autowired
	@Qualifier("finder")
	private JsonDataFinder finder;
	
	//Temporary endpoint to test data fetching
	// TODO remove before before production
	@GetMapping("/testfetchdata")
	public ResponseEntity<Map<String, Object>> testDataFetching() {
		Map<String, Object> data = finder.processQuery();
		return ResponseEntity.ok(data);
	}
}
