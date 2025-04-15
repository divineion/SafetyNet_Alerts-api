package com.safetynet.safetynetalertsapi.services.validators;

import com.safetynet.safetynetalertsapi.exceptions.IdentityMismatchException;
import com.safetynet.safetynetalertsapi.model.dto.PersonDTO;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonValidator {
    public void validateIdentityMatches(PersonDTO personDto, String lastName, String firstName) throws IdentityMismatchException {

        if (!StringFormatter.normalizeString(personDto.getIdentity().toString()).equals(StringFormatter.normalizeString(firstName.concat(lastName)))) {
            throw new IdentityMismatchException("Identity in the request body does not match the parameters in the URL.");
        }
    }
}
