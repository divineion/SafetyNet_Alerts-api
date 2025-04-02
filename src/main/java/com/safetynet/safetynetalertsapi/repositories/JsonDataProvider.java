package com.safetynet.safetynetalertsapi.repositories;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safetynet.safetynetalertsapi.model.DataSet;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;

@Repository
public class JsonDataProvider implements DataProvider {
	private static final Logger logger = LogManager.getLogger(JsonDataProvider.class);
	
	@Autowired
	private DataSetLoader dataSetLoader;

	public DataSet getAllData() {
		return dataSetLoader.getDataSet();
	}
	
	public List<Person> findAllPersons() {
		DataSet data = getAllData();
		logger.info("Retrieving all persons");
		List<Person> persons = data.getPersons();
		return persons;
	}
	
	public List<FireStation> findAllFireStations() {
		DataSet data = getAllData();
		logger.info("Retrieving all firestations");
		List<FireStation> fireStations = data.getFireStations();
		return fireStations;
	}
	
	public List<MedicalRecord> findAllMedicalRecords() {
		DataSet data = getAllData();
		logger.info("Retrieving all medical records");
		List<MedicalRecord> medicalRecords = data.getMedicalRecords();
		return medicalRecords;
	}
}
