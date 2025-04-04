package com.safetynet.safetynetalertsapi.services.validators;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.repositories.JsonDataProvider;

@Service
public class FireStationValidator {
	
	@Autowired
	JsonDataProvider provider;
	
	public boolean stationExists(int stationNumber) {
		List<FireStation> fireStations = provider.getAllData().getFireStations();
		
		boolean exists = fireStations.stream()
		.anyMatch(station -> station.getStation() == stationNumber);
		
		return exists;
	}
}
