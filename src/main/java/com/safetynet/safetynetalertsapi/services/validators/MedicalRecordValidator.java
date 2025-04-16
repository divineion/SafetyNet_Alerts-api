package com.safetynet.safetynetalertsapi.services.validators;

import com.safetynet.safetynetalertsapi.exceptions.IdentityMismatchException;
import com.safetynet.safetynetalertsapi.model.dto.MedicalRecordDTO;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordValidator {
    public void validateIdentityMatches(MedicalRecordDTO medicalRecordDTO, String lastName, String firstName) throws IdentityMismatchException {
        if (!StringFormatter.normalizeString(medicalRecordDTO.getIdentity().toString()).equals(StringFormatter.normalizeString(firstName.concat(lastName)))) {
            throw new IdentityMismatchException("Identity in the request body does not match the parameters in the URL.");
        }
    }
}
