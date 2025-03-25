package com.safetynet.safetynetalertsapi.repositories;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalertsapi.constants.DataBaseFilePaths;

@Repository
public class JsonDataProvider implements DataProvider {
	private static final Logger logger = LogManager.getLogger(JsonDataProvider.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	File file = new File(DataBaseFilePaths.DATABASE_JSON_PATH);
	private Map<String, Object> data;
	
	public Map<String,Object> getAllData() {
		data = null;
		try {
			data = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {});
			logger.info("Query processed ! {} returned \n {}", file.getName(), data);
			
			return data;
			} catch (IOException e) {
				logger.error("Error reading data from Json file {}: {}", file.getName(), e.getMessage());
			}
		return data;
	}
}
