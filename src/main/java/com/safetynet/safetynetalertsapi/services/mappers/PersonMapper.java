package com.safetynet.safetynetalertsapi.services.mappers;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

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
		FamilyMemberDTO memberDTO = new FamilyMemberDTO(member.getIdentity());
		
		return memberDTO;
	}

	public List<PersonInfoDTO> fromPersonsToPersonsInfoDTO(List<Person> persons) {
		// TODO Auto-generated method stub
		List<PersonInfoDTO> personInfoDTOList = persons.stream().map(p-> {
			Identity identity = p.getIdentity();
			MedicalRecord medicalRecord = recordFinder.findByIdentity(identity);
			Address address = p.getAddress();
			int age = Period.between(medicalRecord.getBirthDate(), LocalDate.now()).getYears();
			String email = p.getEmail();
			List<String> allergies = medicalRecord.getAllergies();
			List<String> medications = medicalRecord.getMedications();
			return new PersonInfoDTO(identity, address, age, email, allergies, medications);
		}).collect(Collectors.toList());
		
		return personInfoDTOList;
		
	}
}
