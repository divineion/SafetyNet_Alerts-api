package com.safetynet.safetynetalertsapi.services.finders;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.safetynet.safetynetalertsapi.model.Identity;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.dto.ChildDTO;
import com.safetynet.safetynetalertsapi.model.dto.FamilyMemberDTO;
import com.safetynet.safetynetalertsapi.model.dto.PersonInfoDTO;
import com.safetynet.safetynetalertsapi.repositories.JsonDataHandler;
import com.safetynet.safetynetalertsapi.services.collectionutils.PersonFilterService;
import com.safetynet.safetynetalertsapi.services.mappers.PersonMapper;

@Service
public class PersonFinder {

	Logger logger = LogManager.getLogger(PersonFinder.class);

	@Autowired
	private JsonDataHandler dataHandler;

	@Autowired
	private MedicalRecordFinder medicalRecordFinder;

	@Autowired
	PersonMapper personMapper;
	
	@Autowired
	PersonFilterService filterService;

	public List<Person> findAll() {
		List<Person> data = dataHandler.findAllPersons();
		
		return data;
	}

	public List<PersonInfoDTO> findBy(String lastName) {
		List<Person> persons = findAll().stream().filter(p -> p.getIdentity().getLastName().equalsIgnoreCase(lastName)).collect(Collectors.toList());
		
		List<PersonInfoDTO> personDTOList = personMapper.fromPersonsToPersonsInfoDTO(persons);

		logger.info("Found records with name" + lastName + ": " + persons);

		return personDTOList;
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
		logger.debug("addresse comparée : " + address);
		// Filtrer les personnes par adresse
		List<Person> houseHoldMembers = findAll().stream()
				// la méthode filter attend un boolean
				.filter(person -> person.getAddress().getAddress().replace(" ", "").equalsIgnoreCase(address))
				.collect(Collectors.toList());

		return houseHoldMembers;
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
		List<Person> houseHoldMembers = findAllPersonsByAddress(address);
		List<FamilyMemberDTO> members = houseHoldMembers.stream().map(personMapper::fromPersonToFamilyMemberDTO)
				.collect(Collectors.toList());

		logger.debug("personnes trouvées à cette adresse : " + houseHoldMembers);

		return members;
	}

	public List<ChildDTO> findAllChildrenByAddress(String address) {
		List<FamilyMemberDTO> houseHoldMembers = findHouseHoldMembersByAddress(address);
//			
//			// Filtrer les enfants parmi les personnes trouvéees à l'adresse : 
//			// 	1 : rechercher les dossiers médicaux correspondant aux personnes à l'adresse
			// 	et vérifier si la date de naissance est après la limite
		List<FamilyMemberDTO> childrenList = filterService.filterChildren(houseHoldMembers);

//			// 2 : créer les DTO pour les enfants
		List<ChildDTO> children = childrenList.stream()
				.map(member -> {
					Identity identity = member.getIdentity();
					MedicalRecord medicalRecord = medicalRecordFinder.findByIdentity(identity);
					int age = Period.between(medicalRecord.getBirthDate(), LocalDate.now()).getYears();
					return new ChildDTO(identity, age, houseHoldMembers);
				})
//			// récupérer les résultats
				.collect(Collectors.toList());
		
		return children;
	}
}
