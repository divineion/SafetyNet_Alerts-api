package com.safetynet.safetynetalertsapi.repositories;

import com.safetynet.safetynetalertsapi.exceptions.NoChangesDetectedException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicalRecordRepository {

    private final Logger logger = LogManager.getLogger(MedicalRecordRepository.class);

    @Autowired
    private JsonDataHandler dataHandler;

    @Autowired
    private StringFormatter formatter;

    public MedicalRecord save(MedicalRecord medicalRecord) throws ResourceAlreadyExistsException {
        List<MedicalRecord> medicalRecords = dataHandler.findAllMedicalRecords();

        if (medicalRecords.stream().anyMatch(record -> formatter.normalizeString(record.getIdentity().toString()).equals(formatter.normalizeString(medicalRecord.getIdentity().toString())))) {
            throw new ResourceAlreadyExistsException(String.format("A medical record exists already for %s", medicalRecord.getIdentity()));
        }

        dataHandler.write(medicalRecord);

        return medicalRecord;
    }

    public MedicalRecord update(MedicalRecord medicalRecord) throws ResourceNotFoundException, NoChangesDetectedException {
        List<MedicalRecord> medicalRecords = dataHandler.findAllMedicalRecords();

        if (medicalRecords.stream().noneMatch(record -> formatter.normalizeString(record.getIdentity().toString()).equals(formatter.normalizeString(medicalRecord.getIdentity().toString())))) {
            throw new ResourceNotFoundException(String.format("No medical record exists for %s", medicalRecord.getIdentity()));
        }

        if (medicalRecords.stream().anyMatch(record ->
                record.getIdentity().equals(medicalRecord.getIdentity()) &&
                        record.getBirthDate().equals(medicalRecord.getBirthDate()) &&
                        record.getAllergies().equals(medicalRecord.getAllergies()) &&
                        record.getMedications().equals(medicalRecord.getMedications())
        )) {
            throw new NoChangesDetectedException("The medical record data is identical to the existing record. No changes are required");
        }

        dataHandler.update(medicalRecord);

        return medicalRecord;
    }
}
