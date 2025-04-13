package com.safetynet.safetynetalertsapi.repositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.safetynet.safetynetalertsapi.constants.DataBaseFilePaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalertsapi.model.DataSet;

import jakarta.annotation.PostConstruct;
/**
 * <p>This class is responsible for loading a data set from a JSON file.</p>
 * <p>On application startup, the {@link #loadData()} method is automatically
 *  executed thanks to the {@link PostConstruct} annotation.</p>
 * 
 */
@Service
public class JsonDataSetLoader implements DataSetLoader {
	private static final Logger logger = LogManager.getLogger(JsonDataSetLoader.class);

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private ObjectMapper objectMapper;
	
	private DataSet dataSet;
	
	public DataSet getDataSet() {
		return dataSet;
	}

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    private final File dataFilePath = new File(DataBaseFilePaths.DATABASE_JSON_PATH);

    /**
     *<p>This method loads the dataset from a JSON file located in the resources directory, performing
     *the following steps:</p>
     *<ul>
     *	<li>Uses {@link FileInputStream} to retrieve the file.</li>
     *	<li>Uses {@link ObjectMapper} to deserialize its content.</li>
     *	<li>Stores the resulting data in the {@link DataSet} field.</li>
     *</ul>
     */
    @PostConstruct
    public void loadData() {
        logger.debug("Loading dataset file");
        try (InputStream inputStream = new FileInputStream(dataFilePath)) {
            dataSet = objectMapper.readValue(inputStream, DataSet.class);
            dataSet.sortAll();
            logger.info("File has been successfully loaded");
        } catch (Exception e) {
            logger.error("An error occurred while loading dataset :", e);
		}
	}
}	