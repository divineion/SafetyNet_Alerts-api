package com.safetynet.safetynetalertsapi.controllers;

import com.safetynet.safetynetalertsapi.exceptions.NoChangesDetectedException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.dto.MedicalRecordDTO;
import com.safetynet.safetynetalertsapi.services.persisters.MedicalRecordPersister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MedicalRecordController {
    private final static Logger logger = LogManager.getLogger(MedicalRecordController.class);

    @Autowired
    MedicalRecordPersister persister;

    /**
     * Create - Add a new {@link MedicalRecordDTO}
     * @param medicalRecordDTO An object {@link MedicalRecord}
     * @return The person object saved
     */
    @PostMapping("/medicalrecord")
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@RequestBody MedicalRecordDTO medicalRecordDTO) {
        logger.debug("Attempting to create a medical record for {}", medicalRecordDTO.getIdentity());
        try {
            persister.createMedicalRecord(medicalRecordDTO);
            return ResponseEntity.ok(medicalRecordDTO);
        } catch(ResourceAlreadyExistsException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/medicalrecord/{identity}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(@PathVariable String identity, @RequestBody MedicalRecordDTO medicalRecordDTO) {
        logger.debug("Attempting to update the medical record of {}", medicalRecordDTO.getIdentity());
        try {
            persister.updateMedicalRecord(medicalRecordDTO);
            return ResponseEntity.ok(medicalRecordDTO);
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NoChangesDetectedException e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
