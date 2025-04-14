package com.safetynet.safetynetalertsapi.repositories;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;

import java.util.List;

public interface BaseRepository<T> {
    List<T> findAll();

    T save(T resource) throws ResourceAlreadyExistsException;

    void delete(String param1, String param2) throws ResourceNotFoundException;

    T update(T resource) throws ResourceNotFoundException;
}
