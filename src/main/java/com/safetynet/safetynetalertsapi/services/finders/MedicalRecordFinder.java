package com.safetynet.safetynetalertsapi.services.finders;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.Identity;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.repositories.JsonDataProvider;

@Service
public class MedicalRecordFinder {
	
	Logger logger = LogManager.getLogger(MedicalRecord.class);

	@Autowired
	JsonDataProvider provider;
	
	public List<MedicalRecord> findAll() {
		List<MedicalRecord> medicalRecords = provider.findAllMedicalRecords();
		
		return medicalRecords;
	}

	public MedicalRecord findByIdentity(Identity identity) {
		logger.debug("Recherche du MedicalRecord pour : " + identity);
		MedicalRecord record = findAll()
				.stream()
				.filter(r -> r.getIdentity().toString().replace(" ", "").equalsIgnoreCase(identity.toString().replace(" ", "")))
				.findAny().orElse(null);
		return record;
	}
}
