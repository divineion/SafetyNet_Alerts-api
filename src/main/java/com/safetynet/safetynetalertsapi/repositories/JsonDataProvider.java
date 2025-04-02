package com.safetynet.safetynetalertsapi.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safetynet.safetynetalertsapi.model.DataSet;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;

@Repository
public class JsonDataProvider implements DataProvider {
	
	@Autowired
	private DataSetLoader dataSetLoader;

	public DataSet getAllData() {
		return dataSetLoader.getDataSet();
	}
	
	public List<Person> findAllPersons() {
		DataSet data = getAllData();
		List<Person> persons = data.getPersons();
		return persons;
	}
	
	public List<FireStation> findAllFireStations() {
		DataSet data = getAllData();
		List<FireStation> fireStations = data.getFireStations();
		return fireStations;
	}
	
	public List<MedicalRecord> findAllMedicalRecords() {
		DataSet data = getAllData();
		List<MedicalRecord> medicalRecords = data.getMedicalRecords();
		return medicalRecords;
	}
}
