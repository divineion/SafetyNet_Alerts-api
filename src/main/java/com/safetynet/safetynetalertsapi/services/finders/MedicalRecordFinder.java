package com.safetynet.safetynetalertsapi.services.finders;

import java.util.List;

import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.repositories.MedicalRecordRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.Identity;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.repositories.JsonDataHandler;

@Service
public class MedicalRecordFinder {
	
	Logger logger = LogManager.getLogger(MedicalRecord.class);

	@Autowired
    JsonDataHandler dataHandler;

	@Autowired
	MedicalRecordRepository repository;
	
	public List<MedicalRecord> getAllMedicalRecords() {
		List<MedicalRecord> medicalRecords = repository.findAll();
		
		return medicalRecords;
	}

	public MedicalRecord findByIdentity(Identity identity) {
        MedicalRecord record = null;
        try {
            record = repository.findByIdentity(identity);
        } catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
        }
        return record;
	}
}
