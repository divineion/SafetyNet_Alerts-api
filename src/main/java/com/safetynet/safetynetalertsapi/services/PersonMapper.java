package com.safetynet.safetynetalertsapi.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.dto.FamilyMemberDTO;
import com.safetynet.safetynetalertsapi.model.dto.PersonDTO;

@Service
public class PersonMapper {
	
	public List<PersonDTO> fromPersonsToPersonsDTO(List<Person> persons) {
		List<PersonDTO> personsListDTO = persons.stream()
				.map(this::fromPersonToDTO)
				.collect(Collectors.toList());
		
		return personsListDTO;
	}
	
	private PersonDTO fromPersonToDTO(Person person) {
		return new PersonDTO(person.getIdentity(), person.getAddress(), person.getPhone(), person.getEmail());
	}

	public FamilyMemberDTO fromPersonToFamilyMemberDTO(PersonDTO member) {
		FamilyMemberDTO memberDTO = new FamilyMemberDTO(member.getIdentity());
		
		return memberDTO;
	}
}
