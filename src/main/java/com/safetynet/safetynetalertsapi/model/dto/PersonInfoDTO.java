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

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<String> getAllergies() {
		return allergies;
	}
	
	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}
	
	public List<String> getMedications() {
		return medications;
	}
	
	public void setMedications(List<String> medications) {
		this.medications = medications;
	}
}
