package com.safetynet.safetynetalertsapi.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.Identifiable;
import com.safetynet.safetynetalertsapi.model.Identity;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;

@Service
public class PersonFilterService {

	@Autowired
	private MedicalRecordFinder recordFinder;
	
    /**
     * Dynamically calculate the latest birth date to be considered as a child (18 or younger)
     */
    private LocalDate getLimitDateForChildren() {
        return LocalDate.now().minusYears(19).plusDays(1);
    }

    /**
     * Dynamically calculate the earliest birth date to be considered as an adult (19 or older)
     */
    private LocalDate getLimitDateForAdults() {
        return LocalDate.now().minusYears(19);
    }
	
	/**
	 * Filters and returns all children (18 or younger) from a given list of persons. 
	 * 
	 * @param person a list of person to be filtered ; any type that implements {@link Identifiable}.
	 * @return a List of persons classified as children. 
	 */
	public <T extends Identifiable> List<T> filterChildren(List<T> persons) {

		return persons.stream().filter(p -> {
			Identity identity = p.getIdentity();
			MedicalRecord record = recordFinder.findByIdentity(identity);

			return record != null && record.getBirthDate().isAfter(getLimitDateForChildren());
		}).collect(Collectors.toList());
	}

	/**
	 * Counts the number of adults (older than 18) in a given list of persons. 
	 * 
	 * @param person a list of person to be filtered ; any type that implements {@link Identifiable}.
	 * @return the number of persons classified as adults. 
	 */
	public <T extends Identifiable> long countAdults(List<T> persons) {
		return persons.stream()
			.filter(p -> {
				Identity identity = p.getIdentity();
				MedicalRecord record = recordFinder.findByIdentity(identity);
	
				return record != null && record.getBirthDate().isBefore(getLimitDateForAdults());
			})
			.count();
	}
}
