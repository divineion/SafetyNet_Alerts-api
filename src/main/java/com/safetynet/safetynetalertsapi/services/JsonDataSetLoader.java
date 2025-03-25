package com.safetynet.safetynetalertsapi.services;

import java.io.InputStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Loads data from file
 * 
 * 
 */
@Service
public class JsonDataSetLoader implements DataSetLoader {
	private static Logger logger = LogManager.getLogger(JsonDataSetLoader.class);

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private ObjectMapper objectMapper;
	
	private Map<String, Object> dataSet;
	
	public Map<String, Object> getDataSet() {
		return dataSet;
	}

	public void setDataSet(Map<String, Object> dataSet) {
		this.dataSet = dataSet;
	}

	// TODO Voir annotation @PostConstruct
	public void loadData() {
		logger.debug("Loading dataset file");
		try {
			//  chemin relatif au  resources DIR 
			// mvn clean package pour rebuild en incluant le fichier dans le jar
			// TODO stocker path dans env variable
			Resource dataResource = resourceLoader.getResource("classpath:/database/data.json");
			InputStream datasetInputStream = dataResource.getInputStream();
			
			// mapper le fichier
			Map<String, Object> map = objectMapper.readValue(datasetInputStream,
					new TypeReference<Map<String, Object>>() {});
			
			this.setDataSet(map);
			
			logger.info("File has been successfully loaded");
		} catch (Exception e) {
				logger.error("An error occurred while loading dataset");
		}
	}
}	