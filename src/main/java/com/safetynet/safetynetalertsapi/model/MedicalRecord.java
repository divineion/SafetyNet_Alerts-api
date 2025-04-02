package com.safetynet.safetynetalertsapi.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents the medical record of a {@link Person} person in the SafetyNet Alerts system.
 * This class contains personal medical information such as the person's name, birth date,
 * allergies, and medications. 
 */

public class MedicalRecord implements Identifiable { 
	@JsonUnwrapped
    private Identity identity;
    
	@JsonProperty("birthdate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private LocalDate birthDate;
    
	@JsonProperty("allergies")
    private List<String> allergies;
    
	@JsonProperty("medications")
    private List<String> medications;
    
	public Identity getIdentity() {
		return identity;
	}
	
	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
	
	public LocalDate getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
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
	
	@Override
	public String toString() {
		return 
		 this.identity.toString()
		 + " " + this.birthDate
		 + " " + this.allergies
		 + " " + this.medications;
	}
}
