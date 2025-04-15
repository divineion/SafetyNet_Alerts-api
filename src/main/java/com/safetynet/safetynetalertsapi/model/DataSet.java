package com.safetynet.safetynetalertsapi.model;

import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataSet {
	@JsonProperty("persons")
	private List<Person> persons;
	
	@JsonProperty("firestations")
	private List<FireStation> fireStations;
	
	@JsonProperty("medicalrecords")
	private List<MedicalRecord> medicalRecords;

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public List<FireStation> getFireStations() {
		return fireStations;
	}

	public void setFireStations(List<FireStation> firestations) {
		this.fireStations = firestations;
	}

	public List<MedicalRecord> getMedicalRecords() {
		return medicalRecords;
	}

	public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
		this.medicalRecords = medicalRecords;
	}

	public  void sortAll() {
		fireStations.sort(
				Comparator.comparing(FireStation::getStation)
						.thenComparing(FireStation::getAddress)
		);

		medicalRecords.sort(
				Comparator.comparing((MedicalRecord record) -> record.getIdentity().getLastName())
						.thenComparing(record -> record.getIdentity().getFirstName())
		);

		persons.sort(Comparator.comparing((Person person) -> person.getIdentity().getLastName())
				.thenComparing(person -> person.getIdentity().getFirstName())
		);
	}
}
