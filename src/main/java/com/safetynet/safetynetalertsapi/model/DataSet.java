package com.safetynet.safetynetalertsapi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataSet {
	@JsonProperty("persons")
	private List<Person> persons;
	
	@JsonProperty("firestations")
	private List<FireStation> firestations;
	
	@JsonProperty("medicalrecords")
	private List<MedicalRecord> medicalrecords;

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public List<FireStation> getFirestations() {
		return firestations;
	}

	public void setFirestations(List<FireStation> firestations) {
		this.firestations = firestations;
	}

	public List<MedicalRecord> getMedicalrecords() {
		return medicalrecords;
	}

	public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
		this.medicalrecords = medicalrecords;
	}
}
