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

	//			//Java 8 date/time type `java.time.LocalDate` not supported by default
	//https://www.javacodegeeks.com/jackson-java-8-date-time-localdate-support-issues.html
	//configure ObjectMapper
	private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.enable(SerializationFeature.INDENT_OUTPUT);
	
	@Autowired
	private DataSetLoader dataSetLoader;

	private static final File file = new File(DataBaseFilePaths.DATABASE_JSON_PATH);

	public DataSet getAllData() {
		return dataSetLoader.getDataSet();
	}
	
	public List<Person> findAllPersons() {
		return getAllData().getPersons();
	}
	
	public List<FireStation> findAllFireStations() {
		return getAllData().getFireStations();
	}
	
	public List<MedicalRecord> findAllMedicalRecords() {
		return getAllData().getMedicalRecords();
	}

	public void write(Object resource) {
		try {
			//représenter la structure complète du fichier
			DataSet existingData = mapper.readValue(file, DataSet.class);
			//ajouter les données
			if (resource instanceof Person) {
				existingData.getPersons().add((Person) resource);
			}

			if (resource instanceof FireStation) {
				existingData.getFireStations().add((FireStation) resource);
			}

			if (resource instanceof MedicalRecord) {
				existingData.getMedicalRecords().add((MedicalRecord) resource);
			}
			// réécrire tte la structure mise à jour
			mapper.writeValue(file, existingData);  //

		} catch (
				IOException e) {
			logger.error("Failed to write object of type {}: {}", resource.getClass().getSimpleName(), e.getMessage());
		}
	}

	public void update(Object resource) {
		try {
			DataSet existingData = mapper.readValue(file, DataSet.class);
			// vérifier le type de resource et si OK affecter les propriétés à une nouvelle instance
			if (resource instanceof Person newPerson) {
				//stocker les data en argument
                List<Person> persons = existingData.getPersons();

				//chercher la personne dans la liste
                for (int i = 0; i < persons.size(); i++) {
					Person existingPerson = persons.get(i);

                    //si la personne est trouvée, on la remplace par les prop de la ressource fournie
                    if (newPerson.getIdentity().equals(persons.get(i).getIdentity())) {
						persons.set(i, newPerson);
						break;
                    }
                }
				existingData.setPersons(persons);
			}

			// réécrire tte la structure mise à jour
			mapper.writeValue(file, existingData);

//			if (resource instanceof FireStation) {
//				existingData.getFireStations().add((FireStation) resource);
//			}

//			if (resource instanceof MedicalRecord) {
//				existingData.getMedicalRecords().add((MedicalRecord) resource);
//			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
