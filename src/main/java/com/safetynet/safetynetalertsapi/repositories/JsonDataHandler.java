package com.safetynet.safetynetalertsapi.repositories;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.safetynet.safetynetalertsapi.constants.DataBaseFilePaths;
import com.safetynet.safetynetalertsapi.utils.StringFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safetynet.safetynetalertsapi.model.DataSet;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;

@Repository
public class JsonDataHandler {

    private final Logger logger = LogManager.getLogger(JsonDataHandler.class);

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).enable(SerializationFeature.INDENT_OUTPUT);

    @Autowired
    private DataSetLoader dataSetLoader;

    @Autowired
    private StringFormatter formatter;

    private static final File file = new File(DataBaseFilePaths.DATABASE_JSON_PATH);

    /**
     * Sorts all data collections and writes the updated data to the JSON file.
     * Then reloads the in-memory dataset.
     *
     * @param data The {@link DataSet} to sort and write
     * @throws IOException if writing to file fails
     */
    private void sortAndUpdateDataSet(DataSet data) throws IOException {
        data.sortAll();
        mapper.writeValue(file, data);
        dataSetLoader.loadData();
    }

    /**
     * Returns the entire dataset loaded in memory.
     *
     * @return The full {@link DataSet}
     */
    public DataSet getAllData() {
        return dataSetLoader.getDataSet();
    }

    /**
     * Adds a new resource ({@link Person}, {@link FireStation}, or {@link MedicalRecord}) to the dataset,
     * then sorts and saves the updated data to the JSON file.
     *
     * @param resource The object to add
     */
    public void write(Object resource) {
        try {
            DataSet data = mapper.readValue(file, DataSet.class);
            if (resource instanceof Person) {
                data.getPersons().add((Person) resource);
            }

            if (resource instanceof FireStation) {
                data.getFireStations().add((FireStation) resource);
            }

            if (resource instanceof MedicalRecord) {
                data.getMedicalRecords().add((MedicalRecord) resource);
            }
            sortAndUpdateDataSet(data);

        } catch (IOException e) {
            logger.error("Failed to write object of type {}: {}", resource.getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * Updates an existing resource in the {@link DataSet} by replacing the matching one.
     * <p>Uses identity property (or address for fire stations) to find the match.
     * Then sorts and saves the updated data.</p>
     *
     * @param resource The updated object to save
     */
    public void update(Object resource) {
        try {
            DataSet data = mapper.readValue(file, DataSet.class);
            if (resource instanceof Person newPerson) {
                List<Person> persons = data.getPersons();

                for (int i = 0; i < persons.size(); i++) {
                    Person existingPerson = persons.get(i);

                    if (formatter.normalizeString(newPerson.getIdentity().toString()).equals(formatter.normalizeString(existingPerson.getIdentity().toString()))) {
                        persons.set(i, newPerson);
                        break;
                    }
                }
                data.setPersons(persons);
            }

            if (resource instanceof FireStation newFireStation) {
                List<FireStation> fireStations = data.getFireStations();

                for (int i = 0; i < fireStations.size(); i++) {
                    FireStation existingFireStation = fireStations.get(i);

                    if (newFireStation.getAddress().equals(existingFireStation.getAddress())) {
                        fireStations.set(i, newFireStation);
                        break;
                    }
                }
                data.setFireStations(fireStations);
            }

            if (resource instanceof MedicalRecord newMedicalRecord) {
                List<MedicalRecord> medicalRecords = data.getMedicalRecords();

                for (int i = 0; i < medicalRecords.size(); i++) {
                    MedicalRecord existingMedicalRecord = medicalRecords.get(i);

                    if (newMedicalRecord.getIdentity().equals(existingMedicalRecord.getIdentity())) {
                        medicalRecords.set(i, newMedicalRecord);
                        break;
                    }
                }
                data.setMedicalRecords(medicalRecords);
            }
            sortAndUpdateDataSet(data);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes a resource ({@link Person}, {@link FireStation}, or {@link MedicalRecord}) from the dataset.
     * Then sorts and saves the updated data to the JSON file.
     *
     * @param type   The class type of the resource
     * @param entity The resource object to delete
     * @param <T>    Generic type for resource
     */
    public <T> void delete(T type, T entity) {
        try {
            DataSet data = mapper.readValue(file, DataSet.class);

            if (type.equals(Person.class)) {
                List<Person> persons = data.getPersons();
                persons.remove((Person) entity);
                data.setPersons(persons);
            }

            if (type.equals(FireStation.class)) {
                List<FireStation> fireStations = data.getFireStations();
                fireStations.remove((FireStation) entity);
                data.setFireStations(fireStations);
            }

            if (type.equals(MedicalRecord.class)) {
                List<MedicalRecord> medicalRecords = data.getMedicalRecords();
                medicalRecords.remove((MedicalRecord) entity);
                data.setMedicalRecords(medicalRecords);
            }
            sortAndUpdateDataSet(data);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
