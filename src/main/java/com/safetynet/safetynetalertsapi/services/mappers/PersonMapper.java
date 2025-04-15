package com.safetynet.safetynetalertsapi.services.mappers;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import com.safetynet.safetynetalertsapi.model.dto.ChildDTO;
import com.safetynet.safetynetalertsapi.model.dto.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.Address;
import com.safetynet.safetynetalertsapi.model.Identity;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.dto.FamilyMemberDTO;
import com.safetynet.safetynetalertsapi.model.dto.PersonInfoDTO;
import com.safetynet.safetynetalertsapi.services.finders.MedicalRecordFinder;

@Service
public class PersonMapper {
	
	@Autowired
	MedicalRecordFinder recordFinder;

	public FamilyMemberDTO fromPersonToFamilyMemberDTO(Person member) {
		return new  FamilyMemberDTO(member.getIdentity());
    }

    public List<PersonInfoDTO> fromPersonsToPersonsInfoDtoList(List<Person> persons) {
        return persons.stream().map(p -> {
            Identity identity = p.getIdentity();
            MedicalRecord medicalRecord = recordFinder.findByIdentity(identity);
            Address address = p.getAddress();
            int age = Period.between(medicalRecord.getBirthDate(), LocalDate.now()).getYears();
            String email = p.getEmail();
            List<String> allergies = medicalRecord.getAllergies();
            List<String> medications = medicalRecord.getMedications();

            return new PersonInfoDTO(identity, address, age, email, allergies, medications);
        }).toList();
    }

	public PersonDTO fromPersonToPersonDTO(Person person) {
		return new PersonDTO(person.getIdentity(), person.getAddress(), person.getPhone(), person.getEmail());
	}

	public Person fromPersonDtoToPerson(PersonDTO persondto) {
		return new Person(persondto.getIdentity(), persondto.getAddress(), persondto.getPhone(), persondto.getEmail());
	}

	public ChildDTO fromMemberToChildDto(FamilyMemberDTO child, List<FamilyMemberDTO> houseHoldMembers) {
		Identity identity = child.getIdentity();
		MedicalRecord medicalRecord = recordFinder.findByIdentity(identity);
		int age = Period.between(medicalRecord.getBirthDate(), LocalDate.now()).getYears();
		return new ChildDTO(identity, age, houseHoldMembers);
	}
}
