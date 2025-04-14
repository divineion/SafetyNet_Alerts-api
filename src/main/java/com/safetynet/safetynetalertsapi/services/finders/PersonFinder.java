package com.safetynet.safetynetalertsapi.services.finders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.safetynet.safetynetalertsapi.repositories.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.dto.ChildDTO;
import com.safetynet.safetynetalertsapi.model.dto.FamilyMemberDTO;
import com.safetynet.safetynetalertsapi.model.dto.PersonInfoDTO;
import com.safetynet.safetynetalertsapi.services.collectionutils.PersonFilterService;
import com.safetynet.safetynetalertsapi.services.mappers.PersonMapper;

@Service
public class PersonFinder {

	Logger logger = LogManager.getLogger(PersonFinder.class);

	@Autowired
	private MedicalRecordFinder medicalRecordFinder;

	@Autowired
	PersonMapper personMapper;
	
	@Autowired
	PersonFilterService filterService;

	@Autowired
	PersonRepository repository;

	public List<Person> findAll() {
		return repository.findAll();
	}

	public List<PersonInfoDTO> findByLastName(String lastName) {
		List<Person> persons = repository.findByLastName(lastName);
		return personMapper.fromPersonsToPersonsInfoDTO(persons);
	}

	public List<String> findAllEmail(String city) {
		List<Person> persons = findAll();
		List<String> emailList = new ArrayList<String>();

		for (Person person : persons) {
			emailList.add(person.getEmail());
		}
		return emailList;
	}

	/**
	 * This method retrieves all persons living at a given address
	 * 
	 * @see Person
	 * 
	 * @param address
	 * @return a List of Person
	 */
	public List<Person> findAllPersonsByAddress(String address) {
		return repository.findAllPersonsByAddress(address);
	}
	
	/**
	 *This method uses the findAllPersonsByAddress() method and returns a List of FamilyMembersDTO
	 *
	 * @see FamilyMemberDTO
	 *
	 * @param address
	 * @return a List of FamilyMembersDTO
	 */
	public List<FamilyMemberDTO> findHouseHoldMembersByAddress(String address) {
		List<Person> members = repository.findAllPersonsByAddress(address);
		List<FamilyMemberDTO> dto = members.stream().map(personMapper::fromPersonToFamilyMemberDTO).toList();
		return dto;
	}

	public List<ChildDTO> findAllChildrenByAddress(String address) {
		List<FamilyMemberDTO> houseHoldMembers = findHouseHoldMembersByAddress(address);
		List<FamilyMemberDTO> childrenList = filterService.filterChildren(houseHoldMembers);

		List<ChildDTO> children = childrenList.stream()
				.map(child -> personMapper.fromMemberToChildDto(child, houseHoldMembers))
				.collect(Collectors.toList());
		
		return children;
	}
}
