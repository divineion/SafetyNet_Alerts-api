package com.safetynet.safetynetalertsapi.repositories;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.safetynet.safetynetalertsapi.constants.DataBaseFilePaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safetynet.safetynetalertsapi.model.DataSet;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;

@Repository
public class JsonDataHandler implements DataHandler {

	private final Logger logger = LogManager.getLogger(JsonDataHandler.class);
	
	@Autowired
	private DataSetLoader dataSetLoader;

	private static final File file = new File(DataBaseFilePaths.DATABASE_JSON_PATH);

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

	public void write(Person person) throws IOException {
		//			//Java 8 date/time type `java.time.LocalDate` not supported by default
		//https://www.javacodegeeks.com/jackson-java-8-date-time-localdate-support-issues.html
		//configure ObjectMapper
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.enable(SerializationFeature.INDENT_OUTPUT);

		try {
			//représenter la structure complète du fichier
			DataSet existingData = mapper.readValue(file, DataSet.class);

			//ajouter les données
			existingData.getPersons().add(person);
			// réécrire tte la structure mise à jour
			mapper.writeValue(file, existingData);  //

		} catch (
				IOException e) {
			logger.error(e.getMessage());
		}
	}
}
