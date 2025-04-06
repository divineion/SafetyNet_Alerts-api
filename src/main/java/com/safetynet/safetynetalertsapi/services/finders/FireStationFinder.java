package com.safetynet.safetynetalertsapi.services.finders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.safetynet.safetynetalertsapi.model.dto.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.digester.SystemPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.exceptions.StationNotFoundException;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.repositories.JsonDataHandler;
import com.safetynet.safetynetalertsapi.services.collectionutils.PersonFilterService;
import com.safetynet.safetynetalertsapi.services.mappers.FireStationMapper;
import com.safetynet.safetynetalertsapi.services.validators.FireStationValidator;

import static java.util.stream.Collectors.toList;

@Service
public class FireStationFinder {
	@Autowired
	private JsonDataHandler dataHandler;

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
		List<FireStation> fireStations = dataHandler.findAllFireStations();

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
	 * {@link FireStationFinder#getFireStationAddressesCoverage(int)} method - store
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
	 * This method: - calls {@link FireStationFinder#getFireStationCoverage(int)} to
	 * list the persons covered by a station, - uses
	 * {@link PersonFilterService#filterChildren(List)} to retrieve children, - uses
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
	 * - Collect the unique phone and return the results as a {@link List<String>}.
	 *
	 * @param stationNumber the fire station number
	 * @return a {@link List<String>} containing the fire station number and a list of
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

	/**
	 *Retrieves the fire station number and the list of people to alert for a given address.
	 * <p>
	 * This method:
	 * <ul>
	 *     <li>finds all persons living at the specified address,</li>
	 *     <li>identifies the fire station number serving that address, </li>
	 *     <li>and returns both as a {@link FireAlertDTO}.</li>
	 * </ul>
	 * The returned
	 * list of residents includes their name, phone number, age, medications, and allergies.
	 *
	 * @param address an address where a fire alert is triggered
	 * @return a {@link FireAlertDTO} containing the station number and a list of residents
	 * with their personal and medical information
	 */
	public FireAlertDTO getFireAlertInfoByAddress(String address) {
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

	/**
	 * Returns a list of households served by the specified fire stations,
	 * grouped by address.
	 * <p>Each household includes the list of residents living
	 * at that address, along with their name, phone number, age, and medical
	 * information (medications, dosage, and allergies).</p>
	 *
	 * @param stationNumbers a list of fire station numbers for which to retrieve flood alert information
	 * @return a {@link List} containing a list of {@link FloodAlertDTO},
	 *         each representing an address and its residents to alert in case of flood
	 */
	//https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
	//https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#flatMap-java.util.function.Function-
	//https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#map-java.util.function.Function-
	public List<FloodAlertDTO> getFloodAlertInfoByStations(List<Integer> stationNumbers) {
		// instancier le dto
		List<FloodAlertDTO> floodAlertDTOList = new ArrayList<>();

		// récupérer les adresses couvertes par les différentes casernes
		stationNumbers.forEach(stationNumber -> {
			List<String> coveredAddresses = getFireStationAddressesCoverage(stationNumber)
					.stream()
					.toList();

			//créer un floodAlertDTO pour chaque adresse et ajouter les AlertPersonInfoDTO des résidents pour chaque adresse
			for (String address : coveredAddresses) {
				//FireAlertDTO contient des AlertPersonInfoDTO que je peux récupérer à partir des adresses
				FireAlertDTO alertDTO = getFireAlertInfoByAddress(address.replace(" ", "").toLowerCase());
				List<AlertPersonInfoDTO> persons = alertDTO.getResidents();

				FloodAlertDTO floodAlertDTO = new FloodAlertDTO(address, persons);
				//ajouter à la liste chaque FloodAlertDTO
				floodAlertDTOList.add(floodAlertDTO);
			}
		});
        return floodAlertDTOList;
    }
}
