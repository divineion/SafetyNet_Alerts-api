package com.safetynet.safetynetalertsapi.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.safetynet.safetynetalertsapi.model.DataSet;
import com.safetynet.safetynetalertsapi.services.DataSetLoader;

@Repository
public class JsonDataProvider implements DataProvider {
	private static final Logger logger = LogManager.getLogger(JsonDataProvider.class);
	
	@Autowired
	private DataSetLoader dataSetLoader;
	
	
	public DataSet getAllData() throws JsonProcessingException {
		DataSet data = dataSetLoader.getDataSet();
		logger.debug("Retrieve in-memory data");
		return data;
	}
}
