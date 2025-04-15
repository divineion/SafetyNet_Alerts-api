package com.safetynet.safetynetalertsapi.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.safetynet.safetynetalertsapi.model.Address;
import com.safetynet.safetynetalertsapi.model.Identity;

public class PersonInfoDTO {
	@JsonUnwrapped
	private Identity identity;
	private Address address;
	private int age;
	private String email;
	private List<String> allergies;
	private List<String> medications;

	public PersonInfoDTO(Identity identity, Address address, int age, String email,
			List<String> allergies, List<String> medications) {
		this.identity = identity;
		this.address = address;
		this.age = age;
		this.email = email;
		this.allergies = allergies;
		this.medications = medications;
	}

	public Identity getIdentity() {
		return identity;
	}

	public Address getAddress() {
		return address;
	}

	public int getAge() {
		return age;
	}

	public String getEmail() {
		return email;
	}
	
	public List<String> getAllergies() {
		return allergies;
	}
	
	public List<String> getMedications() {
		return medications;
	}
}
