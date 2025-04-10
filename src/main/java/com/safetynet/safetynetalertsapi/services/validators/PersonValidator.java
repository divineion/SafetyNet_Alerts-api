package com.safetynet.safetynetalertsapi.services.validators;

import com.safetynet.safetynetalertsapi.exceptions.IdentityMismatchException;
import com.safetynet.safetynetalertsapi.model.dto.PersonDTO;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonValidator {

    @Autowired
    StringFormatter formatter;

    public void validateIdentityMatches(PersonDTO personDto, String lastName, String firstName) throws IdentityMismatchException {

        if (!formatter.normalizeString(formatter.normalizeString(personDto.getIdentity().toString())).equals(formatter.normalizeString(firstName.concat(lastName)))) {
            throw new IdentityMismatchException("Identity in the request body does not match the parameters in the URL.");
        }
    }
}
