package com.safetynet.safetynetalertsapi.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.safetynetalertsapi.exceptions.StationNotFoundException;
import com.safetynet.safetynetalertsapi.model.dto.FireAlertDTO;
import com.safetynet.safetynetalertsapi.model.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalertsapi.model.dto.FireStationDTO;
import com.safetynet.safetynetalertsapi.services.finders.FireStationFinder;
import com.safetynet.safetynetalertsapi.model.dto.FloodAlertDTO;

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
	
	@GetMapping("/phonealert/{stationNumber}")
	public ResponseEntity<List<String>> getPhoneCoveredByStation(@PathVariable int stationNumber) {
		try {
			List<String> phoneNumbers;
			phoneNumbers = finder.getCoveredPhone(stationNumber);
			logger.info("Phone numbers found for station " + stationNumber + ": "+ phoneNumbers);
			
			return ResponseEntity.ok(phoneNumbers);
		} catch (StationNotFoundException e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(null);
		}
	}
	
	@GetMapping("/fire/{address}")
	public ResponseEntity<FireAlertDTO> getFireAlertInfoByAddress(@PathVariable String address) {
		FireAlertDTO fire = finder.getFireAlertInfoByAddress(address);
		return ResponseEntity.ok(fire);
	}

	@GetMapping("/flood/{stationNumbers}")
	public ResponseEntity<?> getFloodAlertInfoByStations(@PathVariable List<Integer> stationNumbers) {
		List<FloodAlertDTO> personsToAlert = finder.getFloodAlertInfoByStations(stationNumbers);
		return ResponseEntity.ok(personsToAlert);
	}

	/**
	 * Create - Add a new {@link FireStation} to the data source file.
	 *
	 * @param fireStationDTO an object {@link FireStationDTO} representing a {@link FireStation}
	 * @return The fire station object saved
	 */
	@PostMapping("/firestation")
	public ResponseEntity<FireStationDTO> createFireStation(@RequestBody FireStationDTO fireStationDTO) {
		try {
			FireStationDTO savedFireStation = persister.saveFireStation(fireStationDTO);
			return ResponseEntity.ok(savedFireStation);
		} catch(ResourceAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
    }
}
