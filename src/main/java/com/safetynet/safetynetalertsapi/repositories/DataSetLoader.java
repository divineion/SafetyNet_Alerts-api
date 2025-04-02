package com.safetynet.safetynetalertsapi.repositories;

import com.safetynet.safetynetalertsapi.model.DataSet;

public interface DataSetLoader {
	void loadData();

	DataSet getDataSet();
	}
