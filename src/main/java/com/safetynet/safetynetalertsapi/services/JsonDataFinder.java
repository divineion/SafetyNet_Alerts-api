package com.safetynet.safetynetalertsapi.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.DataSet;
import com.safetynet.safetynetalertsapi.repositories.JsonDataProvider;

@Service
@Qualifier("finder")
public class JsonDataFinder implements DataFinder {
	private static Logger logger = LogManager.getLogger(JsonDataFinder.class);
	
	@Autowired
	JsonDataProvider dataProvider;
		
	public DataSet processQuery() {
		try {
			logger.debug("Processing query");
			DataSet dataSet =  dataProvider.getAllData();
						
			return dataSet;
		} catch(Exception e) {
			logger.error("An error occured while processing query : ", e);
			
			return null;
		}
	}
}
