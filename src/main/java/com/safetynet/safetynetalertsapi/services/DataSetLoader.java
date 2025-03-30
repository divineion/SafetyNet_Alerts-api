package com.safetynet.safetynetalertsapi.services;

import com.safetynet.safetynetalertsapi.model.DataSet;

public interface DataSetLoader {
	void loadData();

	DataSet getDataSet();
	}
