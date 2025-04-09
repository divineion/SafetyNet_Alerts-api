package com.safetynet.safetynetalertsapi.repositories;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;

public interface BaseRepository<T> {
    T save(T resource) throws ResourceAlreadyExistsException;

    void delete(String param1, String param2) throws ResourceNotFoundException;

    T update(T resource) throws ResourceNotFoundException;
}
