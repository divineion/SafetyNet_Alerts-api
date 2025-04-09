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
public class MedicalRecordRepository implements BaseRepository<MedicalRecord> {

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

    public MedicalRecord update(MedicalRecord medicalRecord) throws ResourceNotFoundException {
        List<MedicalRecord> medicalRecords = dataHandler.findAllMedicalRecords();

        if (medicalRecords.stream().noneMatch(record -> record.getIdentity().toString().equalsIgnoreCase(medicalRecord.getIdentity().toString()))) {
            throw new ResourceNotFoundException(String.format("No medical record exists for %s", medicalRecord.getIdentity()));
        }

        dataHandler.update(medicalRecord);

        return medicalRecord;
    }

    public void delete(String lastName, String firstName) throws ResourceNotFoundException {
        List<MedicalRecord> medicalRecords = dataHandler.findAllMedicalRecords();
        String uniqueIdentifier = firstName.concat(lastName);

        MedicalRecord medicalRecordToDelete = medicalRecords
                .stream()
                .filter(record -> formatter.normalizeString(record.getIdentity().toString()).equals(uniqueIdentifier))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No medical record exists for this identifier"));

        dataHandler.delete(MedicalRecord.class, medicalRecordToDelete);
    }
}
