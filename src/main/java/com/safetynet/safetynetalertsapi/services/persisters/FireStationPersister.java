package com.safetynet.safetynetalertsapi.services.persisters;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.dto.FireStationDTO;
import com.safetynet.safetynetalertsapi.repositories.FireStationRepository;
import com.safetynet.safetynetalertsapi.exceptions.InvalidAddressException;
import com.safetynet.safetynetalertsapi.services.mappers.FireStationMapper;
import com.safetynet.safetynetalertsapi.services.validators.FireStationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**Service responsible for handling persistence operations related to {@link FireStation} entities.
 * It provides methods to save, update, and delete fire station records while applying validation and mapping logic.
 */
@Service
public class FireStationPersister {
    @Autowired
    FireStationRepository repository;

    @Autowired
    FireStationMapper mapper;

    @Autowired
    FireStationValidator validator;

    /**
     * Saves a new {@link FireStation} to the database.
     *
     * @param fireStationDTO The DTO containing the fire station information to be saved.
     * @return The saved fire station as a DTO.
     * @throws ResourceAlreadyExistsException If a fire station with the same address and station number already exists.
     */
    public FireStationDTO saveFireStation(FireStationDTO fireStationDTO) throws ResourceAlreadyExistsException {
        FireStation fireStation = mapper.fromFireStationDtoToFireStation(fireStationDTO);
        FireStation savedFireStation = repository.save(fireStation);
        FireStationDTO responseDtoFireStation = mapper.fromFireStationToFireStationDTO(savedFireStation);

        return responseDtoFireStation;
    }

    /**
     * Deletes a {@link FireStation} from the database using its address and station number.
     *
     * @param address       The address of the fire station to delete.
     * @param stationNumber The station number associated with the fire station.
     * @throws ResourceNotFoundException If no fire station is found matching the provided address and station number.
     * @throws RuntimeException          If an unexpected error occurs during deletion.
     */
    public void deleteFireStation(String address, String stationNumber) throws ResourceNotFoundException {
        repository.delete(address, stationNumber);
    }

    /**
     * Updates an existing {@link FireStation} identified by its address.
     *
     * @param fireStationDTO The DTO containing the updated fire station information.
     * @param address        The address of the fire station to update.
     * @return The updated fire station as a DTO.
     * @throws ResourceNotFoundException If no fire station is found for the given address.
     * @throws InvalidAddressException   If the address in the DTO does not match the provided address.
     */
    public FireStationDTO updateFireStation(FireStationDTO fireStationDTO, String address) throws ResourceNotFoundException, InvalidAddressException {
        FireStation fireStation = mapper.fromFireStationDtoToFireStation(fireStationDTO);

        validator.validateFireStationAddressAssociation(fireStation, address);

        FireStation updatedFireStation = repository.update(fireStation);

        FireStationDTO responseFireStation = mapper.fromFireStationToFireStationDTO(updatedFireStation);

        return responseFireStation;
    }
}
