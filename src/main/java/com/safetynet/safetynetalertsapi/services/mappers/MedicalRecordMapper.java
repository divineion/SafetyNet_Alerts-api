package com.safetynet.safetynetalertsapi.services.mappers;

import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.dto.MedicalRecordDTO;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordMapper {
    public MedicalRecordDTO fromMedicalRecordToMedicalRecordDto(MedicalRecord medicalRecord) {
        return new MedicalRecordDTO(medicalRecord.getIdentity(), medicalRecord.getBirthDate(), medicalRecord.getAllergies(), medicalRecord.getMedications());
    }

    public MedicalRecord fromMedicalRecordDtoToMedicalRecord(MedicalRecordDTO medicalRecordDto) {
        return new MedicalRecord(medicalRecordDto.getIdentity(), medicalRecordDto.getBirthDate(), medicalRecordDto.getAllergies(), medicalRecordDto.getMedications());
    }
}
