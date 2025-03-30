package com.safetynet.safetynetalertsapi.repositories;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.safetynet.safetynetalertsapi.model.DataSet;

public interface DataProvider {
	public DataSet getAllData() throws JsonProcessingException;

}
