package com.safetynet.safetynetalertsapi.repositories;

import com.safetynet.safetynetalertsapi.exceptions.IdentityMismatchException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.Identity;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicalRecordRepository implements BaseRepository<MedicalRecord> {
    @Autowired
    private JsonDataHandler dataHandler;

    @Autowired
    private StringFormatter formatter;

    @Override
    public List<MedicalRecord> findAll() {
        return dataHandler.getAllData().getMedicalRecords();
    }

    public MedicalRecord findByIdentity(Identity identity) throws ResourceNotFoundException, IdentityMismatchException {
        MedicalRecord medicalRecord = findAll()
                .stream()
                .filter(r ->formatter.normalizeString(r.getIdentity().toString()).equalsIgnoreCase(formatter.normalizeString(identity.toString())))
                .findAny().orElseThrow(() -> new IdentityMismatchException("Identity in the request body does not match the parameters in the URL."));

        if (findAll().stream().noneMatch(record-> formatter.normalizeString(record.getIdentity().toString()).equals(formatter.normalizeString(identity.toString())))) {
            throw new ResourceNotFoundException(identity + " is not found in the database");
        }

        return medicalRecord;
    }

    public MedicalRecord save(MedicalRecord medicalRecord) throws ResourceAlreadyExistsException {
        List<MedicalRecord> medicalRecords = findAll();

        if (medicalRecords.stream().anyMatch(record -> formatter.normalizeString(record.getIdentity().toString()).equals(formatter.normalizeString(medicalRecord.getIdentity().toString())))) {
            throw new ResourceAlreadyExistsException(String.format("A medical record exists already for %s", medicalRecord.getIdentity()));
        }

        dataHandler.write(medicalRecord);

        return medicalRecord;
    }

    public MedicalRecord update(MedicalRecord medicalRecord) throws ResourceNotFoundException {
        List<MedicalRecord> medicalRecords = findAll();

        if (medicalRecords.stream().noneMatch(record -> record.getIdentity().toString().equalsIgnoreCase(medicalRecord.getIdentity().toString()))) {
            throw new ResourceNotFoundException(String.format("No medical record exists for %s", medicalRecord.getIdentity()));
        }

        dataHandler.update(medicalRecord);

        return medicalRecord;
    }

    public void delete(String lastName, String firstName) throws ResourceNotFoundException {
        List<MedicalRecord> medicalRecords = findAll();
        String uniqueIdentifier = firstName.concat(lastName);

        MedicalRecord medicalRecordToDelete = medicalRecords
                .stream()
                .filter(record -> formatter.normalizeString(record.getIdentity().toString()).equals(uniqueIdentifier))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No medical record exists for this identifier"));

        dataHandler.delete(MedicalRecord.class, medicalRecordToDelete);
    }
}
