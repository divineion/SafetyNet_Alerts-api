package com.safetynet.safetynetalertsapi.services.mappers;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.Identity;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.dto.CoveredPersonDTO;
import com.safetynet.safetynetalertsapi.model.dto.AlertPersonInfoDTO;
import com.safetynet.safetynetalertsapi.model.dto.FireStationDTO;
import com.safetynet.safetynetalertsapi.services.finders.MedicalRecordFinder;

/**
 * Service class responsible for mapping between {@link FireStation} domain objects
 * and their corresponding Data Transfer Objects (DTOs).
 *
 * @author divineion
 * @see FireStation
 * @see FireStationDTO
 * @see MedicalRecordFinder
 */
@Service
public class FireStationMapper {
	
	@Autowired
	MedicalRecordFinder medicalRecordFinder;

	/**
	 * Converts a {@link FireStation} entity to its corresponding {@link FireStationDTO}.
	 *
	 * @param fireStation the fire station entity to convert
	 * @return the converted {@link FireStationDTO}
	 */
	public FireStationDTO fromFireStationToFireStationDTO(FireStation fireStation) {
		return new FireStationDTO(fireStation.getAddress(), fireStation.getStation());
	}

	/**
	 * Converts a list of {@link FireStation} entities to a list of {@link FireStationDTO}.
	 *
	 * @param fireStations the list of fire station entities to convert
	 * @return a list of converted {@link FireStationDTO} objects
	 */
	public List<FireStationDTO> fromFireStationsToFireStationsDTO(List<FireStation> fireStations) {
		return fireStations
				.stream()
				.map(this::fromFireStationToFireStationDTO)
				.toList();
	}

	/**
	 * Converts a {@link Person} entity into a {@link CoveredPersonDTO}, which contains basic info
	 * for each person (identity, address, phone).
	 *
	 * @param person the person to convert
	 * @return the converted {@link CoveredPersonDTO}
	 */
	public CoveredPersonDTO fromPersonDTOToCoveredPersonDTO(Person person) {
		return new CoveredPersonDTO(person.getIdentity(), person.getAddress(), person.getPhone());
	}

	/**
	 * Converts a list of {@link Person} into a list of {@link AlertPersonInfoDTO}, which includes
	 * phone, age, allergies, and medications for each person. Uses {@link MedicalRecordFinder}
	 * to enrich the data.
	 *
	 * @param persons the list of persons to convert
	 * @return a list of {@link AlertPersonInfoDTO} with medical and contact info
	 */
	public List<AlertPersonInfoDTO> fromPersonDTOtoAlertPersonInfoDTO(List<Person> persons){

        return persons.stream().map(person -> {
            Identity identity = person.getIdentity();
            MedicalRecord record = medicalRecordFinder.findByIdentity(identity);
            String phone = person.getPhone();
            int age = Period.between(record.getBirthDate(), LocalDate.now()).getYears();
            return new AlertPersonInfoDTO(identity, phone, age, record.getAllergies(), record.getMedications());
        })
        .toList();
	}

	/**
	 * Converts a {@link FireStationDTO} back into a {@link FireStation} entity.
	 *
	 * @param dto the DTO to convert
	 * @return the corresponding {@link FireStation} entity
	 */
	public FireStation fromFireStationDtoToFireStation(FireStationDTO dto) {
		return new FireStation(dto.getAddress(), dto.getStation());
	}
}
