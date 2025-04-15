package com.safetynet.safetynetalertsapi.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.safetynet.safetynetalertsapi.model.Identifiable;
import com.safetynet.safetynetalertsapi.model.Identity;

public class AlertPersonInfoDTO implements Identifiable {
	@JsonUnwrapped
	private Identity identity;
	private String phone;
	private int age;
	private List<String> allergies;
	private List<String> medications;
	
	public AlertPersonInfoDTO(Identity identity, String phone, int age, List<String> allergies, List<String> medications) {
		this.identity = identity;
		this.phone = phone;
		this.age = age;
		this.allergies = allergies;
		this.medications = medications;
	}
	
	public Identity getIdentity() {
		return identity;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public int getAge() {
		return age;
	}

	public List<String> getAllergies() {
		return allergies;
	}
	
	public List<String> getMedications() {
		return medications;
	}
}
