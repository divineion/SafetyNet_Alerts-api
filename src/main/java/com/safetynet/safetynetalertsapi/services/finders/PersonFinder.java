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
import com.safetynet.safetynetalertsapi.model.dto.PersonDTO;
import com.safetynet.safetynetalertsapi.repositories.JsonDataProvider;
import com.safetynet.safetynetalertsapi.services.collectionutils.PersonFilterService;
import com.safetynet.safetynetalertsapi.services.mappers.PersonMapper;

@Service
public class PersonFinder {

	Logger logger = LogManager.getLogger(PersonFinder.class);

	@Autowired
	private JsonDataProvider dataProvider;

	@Autowired
	private MedicalRecordFinder medicalRecordFinder;

	@Autowired
	PersonMapper personMapper;
	
	@Autowired
	PersonFilterService filterService;

	public List<PersonDTO> findAll() {
		List<Person> persons = dataProvider.findAllPersons();

		return personMapper.fromPersonsToPersonsDTO(persons);
	}

	public List<PersonDTO> findBy(String lastName) {
		List<PersonDTO> persons = findAll().stream()
				// la méthode filter attend un boolean
				.filter(p -> p.getIdentity().getLastName().equalsIgnoreCase(lastName)).collect(Collectors.toList());

		logger.info("Found records with name" + lastName + ": " + persons);

		return persons;
	}

	public List<String> findAllEmail(String city) {
		List<PersonDTO> persons = findAll();
		List<String> emailList = new ArrayList<String>();

		for (PersonDTO person : persons) {
			emailList.add(person.getEmail());
		}
		return emailList;
	}

	/**
	 * This method retrieves all persons living at a given address
	 * 
	 * @see PersonDTO
	 * 
	 * @param address
	 * @return a List of PersonDTO
	 */
	public List<PersonDTO> findAllPersonsByAddress(String address) {
		System.out.println("addresse comparée : " + address);
		// Filtrer les personnes par adresse
		List<PersonDTO> houseHoldMembers = findAll().stream()
				// la méthode filter attend un boolean
				.filter(person -> person.getAddress().getStreet().toString().replace(" ", "").equalsIgnoreCase(address))
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
		List<PersonDTO> houseHoldMembers = findAllPersonsByAddress(address);
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
