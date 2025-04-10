package com.safetynet.safetynetalertsapi.services.validators;

import com.safetynet.safetynetalertsapi.exceptions.IdentityMismatchException;
import com.safetynet.safetynetalertsapi.model.dto.MedicalRecordDTO;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordValidator {

    @Autowired
    StringFormatter formatter;

    public void validateIdentityMatches(MedicalRecordDTO medicalRecordDTO, String lastName, String firstName) throws IdentityMismatchException {
        if (!formatter.normalizeString(medicalRecordDTO.getIdentity().toString()).equals(formatter.normalizeString(firstName.concat(lastName)))) {
            throw new IdentityMismatchException("Identity in the request body does not match the parameters in the URL.");
        }
    }
}
