package com.safetynet.safetynetalertsapi.controllers;

import com.safetynet.safetynetalertsapi.exceptions.IdentityMismatchException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.Identifiable;
import com.safetynet.safetynetalertsapi.model.Identity;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.dto.MedicalRecordDTO;
import com.safetynet.safetynetalertsapi.services.persisters.MedicalRecordPersister;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class MedicalRecordController {
    private final static Logger logger = LogManager.getLogger(MedicalRecordController.class);

    @Autowired
    MedicalRecordPersister persister;

    @Autowired
    private Validator validator;

    private <T extends Identifiable> void validateMedicalRecord(MedicalRecordDTO object) throws ConstraintViolationException {
        Set<ConstraintViolation<Identity>> violations = validator.validate(object.getIdentity());
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    /**
     * Creates a {@link MedicalRecord} for a person identified by their last name and first name.
     *
     * @param medicalRecordDTO representing the data structure of a {@link MedicalRecord}
     * @return The person object saved
     */
    @PostMapping("/medicalrecord")
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@RequestBody MedicalRecordDTO medicalRecordDTO) {
        try {
            validateMedicalRecord(medicalRecordDTO);
            persister.createMedicalRecord(medicalRecordDTO);
            return ResponseEntity.ok(medicalRecordDTO);
        } catch(ResourceAlreadyExistsException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
         catch(ConstraintViolationException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Updates the medical record for a person identified by their last name and first name.
     *
     * @param lastName the lastname of the person
     * @param medicalRecordDTO representing the data structure of a {@link MedicalRecord}
     * @return a {@link ResponseEntity} containing a JSON representation of the updated medical record
     */
    @PutMapping("/medicalrecord/{lastName}/{firstName}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(@PathVariable String lastName, @PathVariable String firstName, @RequestBody MedicalRecordDTO medicalRecordDTO) {
        try {
            validateMedicalRecord(medicalRecordDTO);
            persister.updateMedicalRecord(medicalRecordDTO, lastName, firstName);
            return ResponseEntity.ok(medicalRecordDTO);
        } catch (ConstraintViolationException e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IdentityMismatchException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Deletes the {@link MedicalRecord} for a person identified by their last name and first name.
     *
     * @param lastName the last name of the person related to the medical record
     * @param firstName the first name of the person related to the medical record
     * @return a {@link ResponseEntity} indicating the result of the deletion attempt:
     *  - HTTP status 204 No Content if the record is successfully deleted,
     *  - HTTP status 404 if the record does not exist.
     */
    @DeleteMapping("/medicalrecord/{lastName}/{firstName}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String lastName, @PathVariable String firstName) {
        try {
           persister.deleteMedicalRecord(lastName, firstName);
           return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(ResourceNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
