package com.safetynet.safetynetalertsapi.services.finders;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.exceptions.StationNotFoundException;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.dto.CoveredPersonDTO;
import com.safetynet.safetynetalertsapi.model.dto.AlertPersonInfoDTO;
import com.safetynet.safetynetalertsapi.model.dto.FireAlertDTO;
import com.safetynet.safetynetalertsapi.model.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalertsapi.model.dto.FireStationDTO;
import com.safetynet.safetynetalertsapi.repositories.JsonDataProvider;
import com.safetynet.safetynetalertsapi.services.collectionutils.PersonFilterService;
import com.safetynet.safetynetalertsapi.services.mappers.FireStationMapper;
import com.safetynet.safetynetalertsapi.services.validators.FireStationValidator;

@Service
public class FireStationFinder {
	@Autowired
	private JsonDataProvider provider;

	@Autowired
	private FireStationMapper mapper;
	
	@Autowired
	private FireStationValidator validator;

	@Autowired
	private PersonFinder personFinder;

	@Autowired
	private PersonFilterService filterService;
	
	private final Logger logger = LogManager.getLogger(FireStationFinder.class);

	public List<FireStationDTO> getAllFireStations() {
		List<FireStation> fireStations = provider.findAllFireStations();

		return mapper.fromFireStationsToFireStationsDTO(fireStations);
	}

	/**
	 * This method retrieves all addresses covered by a given station. Steps : - it
	 * gets all fireStations, - filters to retrieve the given station number, -
	 * returns a collection of stations, each station can appear multiple times with
	 * different covered addresses. - Then based on the previous filter process, it
	 * the retrieves all addresses corresponding to the station number.
	 * 
	 * @param stationNumber
	 * @return a list of addresses
	 */
	public List<String> getFireStationAddressesCoverage(int stationNumber) {
		List<FireStationDTO> fireStations = getAllFireStations().stream()
				.filter(fireStationDTO -> fireStationDTO.getStation() == stationNumber).collect(Collectors.toList());

		List<String> addresses = fireStations.stream().map(f -> f.getAddress()).collect(Collectors.toList());

		return addresses;
	}

	/**
	 * Retrieves a list of CoveredPersonDTO that represents covered persons by a
	 * station Steps : - calls
	 * {@link FireStationFinder#getFireStationAddressesCoverage()} method - store
	 * addresses in a collection - for each address, retrieves all persons living at
	 * this address
	 * 
	 * @param stationNumber
	 * @return a list of persons covered by a station
	 */
	public List<CoveredPersonDTO> getCoveredPersons(int stationNumber) {

		List<String> addresses = getFireStationAddressesCoverage(stationNumber);
		List<CoveredPersonDTO> coveredPersons = addresses.stream()
				.flatMap(a -> personFinder.findAllPersonsByAddress(a.toLowerCase().replace(" ", "")).stream())
				.map(mapper::fromPersonDTOToCoveredPersonDTO).collect(Collectors.toList());

		return coveredPersons;
	}

	/**
	 * This method: - calls {@link FireStationFinder#getFireStationCoverage()} to
	 * list the persons covered by a station, - uses
	 * {@link PersonFilterService#filterChildren()} to retrieve children, - uses
	 * count() to count children among them, - uses
	 * {@link PersonFilterService#countAdults(List)} to count the adults among them.
	 * 
	 * @param stationNumber
	 * @return {@link FireStationCoverageDTO} a class composed with a list of
	 *         persons covered by a station and a count of children and adults
	 */
	public FireStationCoverageDTO getFireStationCoverage(int stationNumber) {

		List<CoveredPersonDTO> coveredPersons = getCoveredPersons(stationNumber);

		return new FireStationCoverageDTO(coveredPersons, filterService.countAdults(coveredPersons),
				filterService.filterChildren(coveredPersons).size());
	}
	
	/**
	 * Retrieves a list of unique phone numbers for persons covered by the given station.
	 * 
	 * Steps:
	 * - Call getCoveredPersons() to retrieve a list of CoveredPersonDTOs.
	 * - Stream the list and map each person to their phone number.
	 * - Collect the unique phone and return the results as a {@link CoveredPhoneDTO}.
	 *
	 * @param stationNumber the fire station number
	 * @return a {@link CoveredPhoneDTO} containing the fire station number and a list of
	 * unique phone numbers of covered persons
	 * @throws StationNotFoundException 
	 */
	public List<String> getCoveredPhone(int stationNumber) throws StationNotFoundException {
		if (!validator.stationExists(stationNumber)) {
			throw new StationNotFoundException("Station number " + stationNumber + " not found");
		}
		List<CoveredPersonDTO> coveredPersons = getCoveredPersons(stationNumber);
		
		List<String> coveredPhones = coveredPersons.stream()
		.map(CoveredPersonDTO::getPhone)
		.distinct()
		.collect(Collectors.toList());
		
		return coveredPhones;
	}

	public FireAlertDTO getAllResidentsByAddress(String address) {
		List<Person> persons = personFinder.findAllPersonsByAddress(address);
		
		int stationNumber = getAllFireStations()
				.stream()
				.filter(fs -> fs.getAddress().replace(" ", "").equalsIgnoreCase(address))
				.findAny()
				.orElse(null)
				.getStation();
		
		List<AlertPersonInfoDTO> residents = mapper.fromPersonDTOtoAlertPersonInfoDTO(persons);
		
		return new FireAlertDTO(stationNumber, residents);
	}
}
