package com.safetynet.safetynetalertsapi.services.finders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.safetynet.safetynetalertsapi.exceptions.ResourceNotFoundException;
import com.safetynet.safetynetalertsapi.model.dto.*;
import com.safetynet.safetynetalertsapi.repositories.FireStationRepository;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.services.collectionutils.PersonFilterService;
import com.safetynet.safetynetalertsapi.services.mappers.FireStationMapper;
import com.safetynet.safetynetalertsapi.services.validators.FireStationValidator;

@Service
public class FireStationFinder {
	@Autowired
	private FireStationMapper mapper;
	
	@Autowired
	private FireStationValidator validator;

	@Autowired
	private PersonFinder personFinder;

	@Autowired
	FireStationRepository repository;

	@Autowired
	private PersonFilterService filterService;
	
	private final Logger logger = LogManager.getLogger(FireStationFinder.class);

	public List<FireStationDTO> getAllFireStations() {
		List<FireStation> fireStations = repository.findAll();

		return mapper.fromFireStationsToFireStationsDTO(fireStations);
	}

	/**
	 * This method retrieves all addresses covered by a given station. Steps : - it
	 * gets all fireStations, - filters to retrieve the given station number, -
	 * returns a collection of stations, each station can appear multiple times with
	 * different covered addresses. - Then based on the previous filter process, it
	 * the retrieves all addresses corresponding to the station number.
	 *
	 * @param stationNumber The fire station number
	 * @return List a list of addresses
	 */
	public List<String> getFireStationAddressesCoverage(int stationNumber) {
		List<FireStationDTO> fireStations = getAllFireStations().stream()
				.filter(fireStationDTO -> fireStationDTO.getStation() == stationNumber).toList();

        return fireStations.stream().map(FireStationDTO::getAddress).toList();
	}

	/**
	 * Retrieves a list of CoveredPersonDTO that represents covered persons by a
	 * station Steps : - calls
	 * {@link FireStationFinder#getFireStationAddressesCoverage(int)} method - store
	 * addresses in a collection - for each address, retrieves all persons living at
	 * this address
	 * 
	 * @param stationNumber The fire station number
	 * @return a list of persons covered by a station
	 */
	public List<CoveredPersonDTO> getCoveredPersons(int stationNumber) throws ResourceNotFoundException {

		List<String> addresses = getFireStationAddressesCoverage(stationNumber);

        List<CoveredPersonDTO> list = new ArrayList<>();
        FireStationMapper fireStationMapper = mapper;
        for (String address : addresses) {
            for (Person person : personFinder.findAllPersonsByAddress(StringFormatter.normalizeString(address))) {
                CoveredPersonDTO coveredPersonDTO = fireStationMapper.fromPersonDTOToCoveredPersonDTO(person);
                list.add(coveredPersonDTO);
            }
        }
        return list;
	}

	/**
	 * This method:
	 * <li>calls {@link #getCoveredPersons(int)} to
	 * list the persons covered by a station,</li>
	 * <li>uses {@link PersonFilterService#filterChildren(List)} to retrieve children,</li>
	 * <li>uses <code>size()</code> to count children among them,</li>
	 * <li>uses {@link PersonFilterService#countAdults(List)} to count the adults among them.</li>
	 *
	 * @param stationNumber The fire station number
	 * @return {@link FireStationCoverageDTO} a class composed with a list of
	 *         persons covered by a station and a count of children and adults
	 */
	public FireStationCoverageDTO getFireStationCoverage(int stationNumber) throws ResourceNotFoundException {

		List<CoveredPersonDTO> coveredPersons = getCoveredPersons(stationNumber);

		return new FireStationCoverageDTO(coveredPersons, filterService.countAdults(coveredPersons),
				filterService.filterChildren(coveredPersons).size());
	}
	
	/**
	 * Retrieves a list of unique phone numbers for persons covered by the given station.
	 * <p>
	 * Steps:
	 * <ul>
	 * 	<li>Call getCoveredPersons() to retrieve a list of CoveredPersonDTOs.</li>
	 * 	<li>Stream the list and map each person to their phone number.</li>
	 * 	<li>Collect the unique phone and return the results as a {@link List<String>}.</li>
	 * </ul>
	 *</p>
	 * @param stationNumber The fire station number
	 * @return a {@link List<String>} of unique phone numbers
	 * @throws ResourceNotFoundException if the fire station is not found.
	 */
	public List<String> getCoveredPhone(int stationNumber) throws ResourceNotFoundException {
		if (!validator.stationExists(stationNumber)) {
			throw new ResourceNotFoundException("Station number " + stationNumber + " not found");
		}

        return getCoveredPersons(stationNumber)
				.stream()
				.map(CoveredPersonDTO::getPhone)
				.distinct()
				.toList();
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
	 * @param address an address covered by a fire station
	 * @return a {@link FireAlertDTO} containing the station number and a list of residents
	 * with their personal and medical information
	 */
	public FireAlertDTO getFireAlertInfoByAddress(String address) throws ResourceNotFoundException {
		List<Person> persons = personFinder.findAllPersonsByAddress(address);
		
		int stationNumber = Objects.requireNonNull(getAllFireStations()
                        .stream()
                        .filter(fs -> StringFormatter.normalizeString(fs.getAddress()).equalsIgnoreCase(StringFormatter.normalizeString(address)))
                        .findAny()
                        .orElse(null))
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
	 * @param stationNumbers a list of fire station numbers
	 * @return a {@link List} containing a list of {@link FloodAlertDTO},
	 *         each representing an address and its residents to alert in case of flood
	 */
	public List<FloodAlertDTO> getFloodAlertInfoByStations(List<Integer> stationNumbers) throws ResourceNotFoundException {
		List<FloodAlertDTO> floodAlertDTOList = new ArrayList<>();

        for (Integer stationNumber : stationNumbers) {
            List<String> coveredAddresses = getFireStationAddressesCoverage(stationNumber)
                    .stream()
                    .toList();

            for (String address : coveredAddresses) {
                List<AlertPersonInfoDTO> persons = getFireAlertInfoByAddress(StringFormatter.normalizeString(address)).getPersons();

                FloodAlertDTO floodAlertDTO = new FloodAlertDTO(address, persons);
                floodAlertDTOList.add(floodAlertDTO);
            }
        }
        return floodAlertDTOList;
    }
}
