package com.safetynet.safetynetalertsapi.services.persisters;

import com.safetynet.safetynetalertsapi.exceptions.IdentityMismatchException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.dto.MedicalRecordDTO;
import com.safetynet.safetynetalertsapi.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalertsapi.services.mappers.MedicalRecordMapper;
import com.safetynet.safetynetalertsapi.services.validators.MedicalRecordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordPersister {
    @Autowired
    MedicalRecordRepository repository;

    @Autowired
    MedicalRecordMapper mapper;

    @Autowired
    MedicalRecordValidator validator;

    /**
     * @param medicalRecordDTO representing the {@link MedicalRecord} structure
     * @return savedMedicalRecord the saved {@link MedicalRecordDTO}
     * @throws ResourceAlreadyExistsException
     */
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO medicalRecordDTO) throws ResourceAlreadyExistsException {
        MedicalRecord medicalRecord = mapper.fromMedicalRecordDtoToMedicalRecord(medicalRecordDTO);

        MedicalRecord savedMedicalRecord = repository.save(medicalRecord);

        return mapper.fromMedicalRecordToMedicalRecordDto(savedMedicalRecord);
    }

    /**
     * @param medicalRecordDTO representing the {@link MedicalRecord} structure
     * @return updatedMedicalRecord the updated {@link MedicalRecordDTO}
     */
    public MedicalRecordDTO updateMedicalRecord(MedicalRecordDTO medicalRecordDTO, String lastName, String firstName) throws ResourceNotFoundException, IdentityMismatchException {
        validator.validateIdentityMatches(medicalRecordDTO, lastName, firstName);
        MedicalRecord medicalRecord = mapper.fromMedicalRecordDtoToMedicalRecord(medicalRecordDTO);

        MedicalRecord updatedMedicalRecord = repository.update(medicalRecord);

        return mapper.fromMedicalRecordToMedicalRecordDto(updatedMedicalRecord);
    }

    public void deleteMedicalRecord(String lastName, String firstName) throws ResourceNotFoundException {
        repository.delete(lastName, firstName);
    }
}
