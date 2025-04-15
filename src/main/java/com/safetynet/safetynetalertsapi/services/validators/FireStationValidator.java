package com.safetynet.safetynetalertsapi.services.validators;

import java.util.List;

import com.safetynet.safetynetalertsapi.exceptions.InvalidAddressException;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.repositories.JsonDataHandler;

@Service
public class FireStationValidator {
	
	@Autowired
	JsonDataHandler dataHandler;
	
	public boolean stationExists(int stationNumber) {
		List<FireStation> fireStations = dataHandler.getAllData().getFireStations();
		
		boolean exists = fireStations.stream()
		.anyMatch(station -> station.getStation() == stationNumber);
		
		return exists;
	}

	/**
	 * Ensures the provided address from the URL matches the one in the FireStation object (from the request body).
	 *
	 * @param fireStation
	 * @param address
	 * @throws InvalidAddressException
	 */
	public void validateFireStationAddressAssociation(FireStation fireStation, String address) throws InvalidAddressException {
		if (!StringFormatter.normalizeString(address).equals(StringFormatter.normalizeString(fireStation.getAddress()))) {
			throw new InvalidAddressException("The provided address in the request URL does not match the provided fire station address.");
		}
	}

}
