package com.safetynet.safetynetalertsapi.services.persisters;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.dto.FireStationDTO;
import com.safetynet.safetynetalertsapi.repositories.FireStationRepository;
import com.safetynet.safetynetalertsapi.repositories.InvalidAddressException;
import com.safetynet.safetynetalertsapi.services.mappers.FireStationMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FireStationPersister {

    private final Logger logger = LogManager.getLogger(FireStationPersister.class);

    @Autowired
    FireStationRepository repository;

    @Autowired
    FireStationMapper mapper;


    public FireStationDTO saveFireStation(FireStationDTO fireStationDTO) throws ResourceAlreadyExistsException {
        FireStation fireStation = mapper.fromFireStationDtoToFireStation(fireStationDTO);
        try {
            FireStation savedFireStation = repository.save(fireStation);

            FireStationDTO responseDtoFireStation = mapper.fromFireStationToFireStationDTO(savedFireStation);

            return responseDtoFireStation;
        } catch (IOException e) {
            logger.error(e.getMessage());

            throw new RuntimeException();
        }
    }

    public void deleteFireStation(String identifier) throws ResourceNotFoundException, RuntimeException {
        repository.delete(identifier);
    }

    public FireStationDTO updateFireStation(FireStationDTO fireStationDTO, String address) throws ResourceNotFoundException, InvalidAddressException {
        FireStation fireStation = mapper.fromFireStationDtoToFireStation(fireStationDTO);

        FireStation updatedFireStation = repository.update(fireStation, address);

        FireStationDTO responseFireStation = mapper.fromFireStationToFireStationDTO(updatedFireStation);

        return responseFireStation;
    }
}
