package com.safetynet.safetynetalertsapi.repositories;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FireStationRepository implements BaseRepository<FireStation> {
    @Autowired
    JsonDataHandler dataHandler;

    @Override
    public List<FireStation> findAll() {
            List<FireStation> fireStations = dataHandler.getAllData().getFireStations();

            return fireStations;
    }

    public FireStation save(FireStation fireStation) throws ResourceAlreadyExistsException {
        List<FireStation> fireStations = findAll();

        if (fireStations.stream().anyMatch(fs -> {
            boolean match = fs.equals(fireStation);

            return match;
        })) {
            throw new ResourceAlreadyExistsException(fireStation.toString() + " is already in the database");
        }
        dataHandler.write(fireStation);

        return fireStation;
    }

    public void delete(String address, String stationNumber) throws ResourceNotFoundException {
        List<FireStation> fireStations = findAll();
        String identifier = address.concat(stationNumber);

        FireStation fireStationToDelete = fireStations
                .stream()
                .filter(fs -> StringFormatter.normalizeString(fs.toString()).equals(StringFormatter.normalizeString(identifier)))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("The station with the provided address and station number is not found."));

        dataHandler.delete(FireStation.class, fireStationToDelete);
    }


    public FireStation update(FireStation fireStation) throws ResourceNotFoundException {
        List<FireStation> fireStations = findAll();

        fireStations
                .stream()
                .filter(fs -> fs.getAddress().equals(fireStation.getAddress()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No resource was found for the provided address"));

        dataHandler.update(fireStation);

        return fireStation;
    }
}
