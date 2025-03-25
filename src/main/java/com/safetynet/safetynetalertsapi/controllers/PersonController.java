package com.safetynet.safetynetalertsapi.controllers;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.safetynetalertsapi.services.JsonDataFinder;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class PersonController {
	
	private static Logger logger = LogManager.getLogger(PersonController.class);
	
	@Autowired
	@Qualifier("finder")
	private JsonDataFinder finder;
	
	// Temporary endpoint to test data fetching
	// TODO remove or modify before production
	// TODO change logger messages
	@GetMapping("/testfetchdata")
	public ResponseEntity<Map<String, Object>> testDataFetching() {
		logger.debug("Request received");
		Map<String, Object> data = finder.processQuery();
		logger.info("Data has been fetched successfully");
		return ResponseEntity.ok(data);
	}
}
