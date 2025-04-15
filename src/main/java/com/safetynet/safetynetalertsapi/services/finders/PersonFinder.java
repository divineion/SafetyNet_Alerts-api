package com.safetynet.safetynetalertsapi.services.finders;

import java.util.ArrayList;
import java.util.List;

import com.safetynet.safetynetalertsapi.repositories.PersonRepository;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.dto.ChildDTO;
import com.safetynet.safetynetalertsapi.model.dto.FamilyMemberDTO;
import com.safetynet.safetynetalertsapi.model.dto.PersonInfoDTO;
import com.safetynet.safetynetalertsapi.services.collectionutils.PersonFilterService;
import com.safetynet.safetynetalertsapi.services.mappers.PersonMapper;

/**
 * Service class providing search operations related to {@link Person} data.
 * <p>
 * This class acts as a finder service layer between controllers and repositories,
 * handling more complex lookup logic and combining results with mapping and filtering services.
 * </p>
 *
 * @author divineion
 * @see Person
 * @see PersonRepository
 * @see PersonMapper
 * @see PersonFilterService
 */
@Service
public class PersonFinder {
	@Autowired
	PersonMapper personMapper;
	
	@Autowired
	PersonFilterService filterService;

	@Autowired
	PersonRepository repository;

	@Autowired
	StringFormatter formatter;

	/**
	 * Retrieves all persons in the data source file.
	 *
	 * @return a list of all {@link Person} instances
	 */
	public List<Person> findAll() {
		return repository.findAll();
	}

	/**
	 * Finds persons by last name and maps them to {@link PersonInfoDTO}.
	 *
	 * @param lastName the last name to filter by
	 * @return a list of {@link PersonInfoDTO} matching the provided last name
	 */
	public List<PersonInfoDTO> findByLastName(String lastName) throws ResourceNotFoundException {
		List<Person> persons = repository.findByLastName(lastName);
		return personMapper.fromPersonsToPersonsInfoDtoList(persons);
	}

	/**
	 * Retrieves the email addresses of all persons living in a specific city.
	 *
	 * @param city the city to search in
	 * @return a list of email addresses
	 */
	public List<String> findEmailListByCity(String city) throws ResourceNotFoundException {
		List<Person> persons = findAll();
		List<String> emailList = new ArrayList<String>();

		if (persons.stream().noneMatch(p -> StringFormatter.normalizeString(p.getAddress().getCity()).equals(StringFormatter.normalizeString(city)))) {
			throw new ResourceNotFoundException("The provided city matches no address among persons' addresses");
		}

		for (Person person : persons) {
			if (formatter.normalizeString(person.getAddress().getCity()).equals(formatter.normalizeString(city))) {
				emailList.add(person.getEmail());
			}
		}
		return emailList;
	}

	/**
	 * Retrieves all persons living at a given address.
	 *
	 * @param address the street address
	 * @return a list of {@link Person} living at the given address
	 */
	public List<Person> findAllPersonsByAddress(String address) {
		return findAll()
				.stream()
				.filter(person -> formatter.normalizeString(person.getAddress().getAddress()).equals(formatter.normalizeString(address)))
				.toList();	}

	/**
	 * Retrieves all household members living at a given address
	 * and maps them to {@link FamilyMemberDTO}.
	 *
	 * @param address the street address
	 * @return a list of {@link FamilyMemberDTO} representing the household members
	 */
	public List<FamilyMemberDTO> findHouseHoldMembersByAddress(String address) throws ResourceNotFoundException {
		List<Person> members = findAllPersonsByAddress(address);
		return members.stream().map(personMapper::fromPersonToFamilyMemberDTO).toList();
	}

	/**
	 *Retrieves a List of children living at a given address
	 *
	 * @param address an address (street only)
	 * @return a List of {@link ChildDTO}
	 */
	public List<ChildDTO> findAllChildrenByAddress(String address) throws ResourceNotFoundException {
		List<FamilyMemberDTO> houseHoldMembers = findHouseHoldMembersByAddress(address);
		List<FamilyMemberDTO> childrenList = filterService.filterChildren(houseHoldMembers);

		return childrenList.stream()
				.map(child -> personMapper.fromMemberToChildDto(child, houseHoldMembers))
				.toList();
	}
}
