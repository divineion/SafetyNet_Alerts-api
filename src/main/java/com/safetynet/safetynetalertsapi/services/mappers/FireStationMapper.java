package com.safetynet.safetynetalertsapi.services.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.dto.CoveredPersonDTO;
import com.safetynet.safetynetalertsapi.model.dto.FireStationDTO;
import com.safetynet.safetynetalertsapi.model.dto.PersonDTO;

@Service
public class FireStationMapper {
	
	public FireStationDTO fromFireStationToFireStationDTO(FireStation fireStation) {
		return new FireStationDTO(fireStation.getAddress(), fireStation.getStation());
	}
	
	public List<FireStationDTO> fromFireStationsToFireStationsDTO(List<FireStation> fireStations) {
		return fireStations
				.stream()
				.map(this::fromFireStationToFireStationDTO)
				.collect(Collectors.toList());
	}
	
	public CoveredPersonDTO fromPersonDTOToCoveredPersonDTO(PersonDTO person) {
		return new CoveredPersonDTO(person.getIdentity(), person.getAddress(), person.getPhone());
	}
	
	public List<CoveredPersonDTO> fromCoveredPersonstoCoveredPersonsDTO(List<PersonDTO> persons) {
		return persons
				.stream()
				.map(this::fromPersonDTOToCoveredPersonDTO)
				.collect(Collectors.toList());
	}
}
