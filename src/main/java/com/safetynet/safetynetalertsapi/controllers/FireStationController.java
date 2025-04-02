package com.safetynet.safetynetalertsapi.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.safetynetalertsapi.model.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalertsapi.model.dto.FireStationDTO;
import com.safetynet.safetynetalertsapi.services.FireStationFinder;

@RestController
public class FireStationController {
	
	private Logger logger = LogManager.getLogger(FireStationController.class);
	
	@Autowired
	private FireStationFinder finder;
	
	@GetMapping("/firestations")
	public ResponseEntity<List<FireStationDTO>> getAllFireStations() {
		List<FireStationDTO> fireStations = finder.getAllFireStations();
		
		return ResponseEntity.ok(fireStations);
	}
	
	@GetMapping("/firestation/{stationNumber}")
	public ResponseEntity<FireStationCoverageDTO> getPersonsCoveredByStation(@PathVariable int stationNumber) {
		FireStationCoverageDTO fireStationcoverage = finder.getFireStationCoverage(stationNumber);
		logger.info("Found " + fireStationcoverage.getAdultsCounter() + " adults and " + fireStationcoverage.getChildrenCounter() + " children");
		return ResponseEntity.ok(fireStationcoverage);
	}
}
