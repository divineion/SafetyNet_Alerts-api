package com.safetynet.safetynetalertsapi.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents the medical record of a {@link Person} person in the SafetyNet Alerts system.
 * This class contains personal medical information such as the person's name, birth date,
 * allergies, and medications. 
 */

public class MedicalRecord {    
    private Identity identity;
    
    private LocalDate birthDate;
    
    private List<String> allergies;
    
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
		"firstName: " + this.identity.getFirstName()
		 + "\n lastName: " + this.identity.getLastName()
		 + "\n birthDate: " + this.birthDate
		 + "\n allergies: " + this.allergies
		 + "\n medication: " + this.medications;
	}
}
