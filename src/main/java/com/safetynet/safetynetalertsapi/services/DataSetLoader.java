package com.safetynet.safetynetalertsapi.services;

import java.util.Map;

public interface DataSetLoader {
	void loadData();
	
	Map<String, Object> getDataSet();
}
