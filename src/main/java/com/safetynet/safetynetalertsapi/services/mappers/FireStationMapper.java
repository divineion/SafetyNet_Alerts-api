package com.safetynet.safetynetalertsapi.services.mappers;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.Identity;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.dto.CoveredPersonDTO;
import com.safetynet.safetynetalertsapi.model.dto.AlertPersonInfoDTO;
import com.safetynet.safetynetalertsapi.model.dto.FireStationDTO;
import com.safetynet.safetynetalertsapi.model.dto.PersonDTO;
import com.safetynet.safetynetalertsapi.services.finders.MedicalRecordFinder;

@Service
public class FireStationMapper {
	
	@Autowired
	MedicalRecordFinder medicalRecordFinder;
	
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
	
	public List<AlertPersonInfoDTO> fromPersonDTOtoAlertPersonInfoDTO(List<PersonDTO> persons) {
		List<AlertPersonInfoDTO> residents = persons.stream().map(person -> {
			Identity identity = person.getIdentity();
			MedicalRecord record = medicalRecordFinder.findByIdentity(identity);
			String phone = person.getPhone();
			int age = Period.between(record.getBirthDate(), LocalDate.now()).getYears();
			List<String> allergies = record.getAllergies();
			List<String> medications = record.getMedications();
			return new AlertPersonInfoDTO(identity, phone, age, allergies, medications);
		})
		.collect(Collectors.toList());
		
		return residents;
	}
}
