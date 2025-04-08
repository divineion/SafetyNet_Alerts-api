package com.safetynet.safetynetalertsapi.services.persisters;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.dto.MedicalRecordDTO;
import com.safetynet.safetynetalertsapi.repositories.MedicalRecordRepository;
import com.safetynet.safetynetalertsapi.services.mappers.MedicalRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordPersister {
    @Autowired
    MedicalRecordRepository repository;

    @Autowired
    MedicalRecordMapper mapper;

    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO medicalRecordDTO) throws ResourceAlreadyExistsException {
        MedicalRecord medicalRecord = mapper.fromMedicalRecordDtoToMedicalRecord(medicalRecordDTO);

        MedicalRecord savedMedicalRecord = repository.save(medicalRecord);

        return mapper.fromMedicalRecordToMedicalRecordDto(savedMedicalRecord);
    }
}
