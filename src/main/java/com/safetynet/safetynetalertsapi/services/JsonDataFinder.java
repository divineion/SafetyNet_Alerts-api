package com.safetynet.safetynetalertsapi.services;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.repositories.JsonDataProvider;

@Service
@Qualifier("finder")
public class JsonDataFinder implements DataFinder {
	private static Logger logger = LogManager.getLogger(JsonDataFinder.class);
	
	@Autowired
	JsonDataProvider dataProvider;
		
	public Map<String, Object> processQuery() {
		try {
			logger.debug("Processing query");
			Map<String, Object> data =  dataProvider.getAllData();
		return data;
		} catch(Exception e) {
			logger.error("An error occured while processing query : ", e);
			
			return null;
		}
	}
}
