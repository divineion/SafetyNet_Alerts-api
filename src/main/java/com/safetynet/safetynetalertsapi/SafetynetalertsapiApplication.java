package com.safetynet.safetynetalertsapi;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.safetynet.safetynetalertsapi.services.DataSetLoader;

@SpringBootApplication
public class SafetynetalertsapiApplication implements CommandLineRunner {
	
	@Autowired
	DataSetLoader dataSetLoader;
	
	public static void main(String[] args) {
		SpringApplication.run(SafetynetalertsapiApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException {
		dataSetLoader.loadData();
	}
}
