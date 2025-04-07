package com.safetynet.safetynetalertsapi.repositories;

import com.safetynet.safetynetalertsapi.exceptions.ResourceAlreadyExistsException;
import com.safetynet.safetynetalertsapi.model.FireStation;
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

    public FireStation save(FireStation fireStation) throws ResourceAlreadyExistsException, IOException {
        List<FireStation> fireStations = dataHandler.findAllFireStations();

        System.out.println("fire station to string : "+fireStation.toString());

        if (fireStations.stream().anyMatch(fs -> {
            boolean match = fs.equals(fireStation);
            System.out.println("Comparing: " + fs + " with " + fireStation + " => " + match);

            return match;
        }))
        {
            throw new ResourceAlreadyExistsException(fireStation.toString() + " is already in the database");
        }
        dataHandler.write(fireStation);

        return fireStation;
    }
}
