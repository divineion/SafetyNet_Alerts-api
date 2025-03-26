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

import jakarta.annotation.PostConstruct;
/**
 * <p>This class is responsible for loading a data set from a JSON file.</p>
 * <p>On application startup, the {@link #loadData()} method is automatically
 *  executed thanks to the {@link PostConstruct} annotation.</p>
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
	
	/**
	 *<p>This method loads the dataset from a JSON file located in the classpath, performing
	 *the following steps:</p>
	 *<ul>
	 *	<li>Uses {@link ResourceLoader} to retrieve the file from the classpath.</li>
	 *	<li>Uses {@link ObjectMapper} to deserialize its content into a {@link Map}.</li>
	 *	<li>Stores the resulting data in the {@code dataSet} field.</li>
	 *</ul> 
	 */
	@PostConstruct
	public void loadData() {
		logger.debug("Loading dataset file");
		try {
			Resource dataResource = resourceLoader.getResource("classpath:/database/data.json");
			InputStream datasetInputStream = dataResource.getInputStream();
			
			Map<String, Object> map = objectMapper.readValue(datasetInputStream,
					new TypeReference<Map<String, Object>>() {});
			
			this.setDataSet(map);
			
			logger.info("File has been successfully loaded");
		} catch (Exception e) {
				logger.error("An error occurred while loading dataset");
		}
	}
}	