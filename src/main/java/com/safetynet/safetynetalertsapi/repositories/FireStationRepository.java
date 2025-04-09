package com.safetynet.safetynetalertsapi.repositories;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class FireStationRepository {
    public final Logger logger = LogManager.getLogger(FireStationRepository.class);

    @Autowired
    JsonDataHandler dataHandler;

    @Autowired
    StringFormatter formatter;

    public FireStation save(FireStation fireStation) throws ResourceAlreadyExistsException, IOException {
        List<FireStation> fireStations = dataHandler.findAllFireStations();

        if (fireStations.stream().anyMatch(fs -> {
            boolean match = fs.equals(fireStation);
            System.out.println("Comparing: " + fs + " with " + fireStation + " => " + match);

            return match;
        })) {
            throw new ResourceAlreadyExistsException(fireStation.toString() + " is already in the database");
        }
        dataHandler.write(fireStation);

        return fireStation;
    }

    public void delete(String identifier) throws ResourceNotFoundException, RuntimeException {
        List<FireStation> fireStations = dataHandler.findAllFireStations();

        //REFACTO : simplifier la vérification et passer directemet l'objet à supprimer
        FireStation fireStationToDelete = fireStations
                .stream()
                .filter(fs -> formatter.normalizeString(fs.toString()).equals(identifier))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("The station with the provided address and station number is not found."));

        dataHandler.delete(FireStation.class, fireStationToDelete);
    }

    public FireStation update(FireStation fireStation, String address) throws ResourceNotFoundException, InvalidAddressException {
        if (!formatter.normalizeString(fireStation.getAddress()).equals(formatter.normalizeString(address))) {
            throw new InvalidAddressException("The provided address in the request does not match the fire station address.");
        }

        List<FireStation> fireStations = dataHandler.findAllFireStations();

        if (fireStations.stream().noneMatch(fs -> {
            return (formatter.normalizeString(fs.getAddress()).equals(formatter.normalizeString(address)));
        })) {
            throw new ResourceNotFoundException("No station exists for the provided address.");
        }

        dataHandler.update(fireStation);

        return fireStation;
    }
}
