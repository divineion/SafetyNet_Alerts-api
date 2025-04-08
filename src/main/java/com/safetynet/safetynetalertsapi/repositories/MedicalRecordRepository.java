package com.safetynet.safetynetalertsapi.repositories;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
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
            throw new ResourceAlreadyExistsException("A medical record exists already for " + medicalRecord.getIdentity());
        }

        dataHandler.write(medicalRecord);

        return medicalRecord;
    }

}
