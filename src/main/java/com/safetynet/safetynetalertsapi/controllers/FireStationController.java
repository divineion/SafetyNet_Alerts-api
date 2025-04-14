package com.safetynet.safetynetalertsapi.controllers;

import java.util.List;

import com.safetynet.safetynetalertsapi.exceptions.*;
import com.safetynet.safetynetalertsapi.services.persisters.FireStationPersister;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safetynet.safetynetalertsapi.model.dto.FireAlertDTO;
import com.safetynet.safetynetalertsapi.model.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalertsapi.model.dto.FireStationDTO;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.services.finders.FireStationFinder;
import com.safetynet.safetynetalertsapi.model.dto.FloodAlertDTO;

@RestController
public class FireStationController {

    private final Logger logger = LogManager.getLogger(FireStationController.class);

    @Autowired
    private FireStationFinder finder;

    @Autowired
    private FireStationPersister persister;

    @Autowired
    StringFormatter formatter;

    @GetMapping("/firestation/{stationNumber}")
    public ResponseEntity<FireStationCoverageDTO> getPersonsCoveredByStation(@PathVariable int stationNumber) {
        FireStationCoverageDTO fireStationcoverage = finder.getFireStationCoverage(stationNumber);
        return ResponseEntity.ok(fireStationcoverage);
    }

    @GetMapping("/phonealert/{stationNumber}")
    public ResponseEntity<List<String>> getPhoneCoveredByStation(@PathVariable int stationNumber) throws ResourceNotFoundException {
        try {
            List<String> phoneNumbers;
            phoneNumbers = finder.getCoveredPhone(stationNumber);
            return ResponseEntity.ok(phoneNumbers);
        } catch (ResourceNotFoundException e) {
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
    public ResponseEntity<List<FloodAlertDTO>> getFloodAlertInfoByStations(@PathVariable List<Integer> stationNumbers) {
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
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Update - Update a {@link FireStation}
     *
     * @param address - The address associated to a station
     * @return a {@link FireStationDTO}
     */
    @PutMapping("/firestation/{address}")
    public ResponseEntity<FireStationDTO> updateFireStation(@PathVariable String address, @RequestBody FireStationDTO fireStationDTO) {
        try {
            FireStationDTO updatedFireStation = persister.updateFireStation(fireStationDTO, address);
            return ResponseEntity.ok(updatedFireStation);
        } catch (ResourceNotFoundException | InvalidAddressException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Delete - Remove an association between a station number and an address
     *
     * @param address       - The address associated to the station
     * @param stationNumber - The station number associated to the given
     */
    @DeleteMapping("/firestation/{stationNumber}/{address}")
    public HttpEntity<?> deleteFireStation(@PathVariable String address, @PathVariable String stationNumber) {
        try {
            persister.deleteFireStation(address, stationNumber);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            logger.error("The specified address {} and station number {} are not associated.", address, stationNumber);
            return ResponseEntity.status(404).build();
        }
    }
}
