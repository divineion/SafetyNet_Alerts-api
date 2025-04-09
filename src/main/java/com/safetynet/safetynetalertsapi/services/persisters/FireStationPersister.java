package com.safetynet.safetynetalertsapi.services.persisters;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.dto.FireStationDTO;
import com.safetynet.safetynetalertsapi.repositories.FireStationRepository;
import com.safetynet.safetynetalertsapi.repositories.InvalidAddressException;
import com.safetynet.safetynetalertsapi.services.mappers.FireStationMapper;
import com.safetynet.safetynetalertsapi.services.validators.FireStationValidator;
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

    @Autowired
    FireStationValidator validator;


    public FireStationDTO saveFireStation(FireStationDTO fireStationDTO) throws ResourceAlreadyExistsException {
        FireStation fireStation = mapper.fromFireStationDtoToFireStation(fireStationDTO);
        FireStation savedFireStation = repository.save(fireStation);
        FireStationDTO responseDtoFireStation = mapper.fromFireStationToFireStationDTO(savedFireStation);

        return responseDtoFireStation;
    }

    public void deleteFireStation(String address, String stationNumber) throws ResourceNotFoundException, RuntimeException {
        repository.delete(address, stationNumber);
    }

    public FireStationDTO updateFireStation(FireStationDTO fireStationDTO, String address) throws ResourceNotFoundException, InvalidAddressException {
        FireStation fireStation = mapper.fromFireStationDtoToFireStation(fireStationDTO);

        validator.validateFireStationAddressAssociation(fireStation, address);

        FireStation updatedFireStation = repository.update(fireStation);

        FireStationDTO responseFireStation = mapper.fromFireStationToFireStationDTO(updatedFireStation);

        return responseFireStation;
    }
}
