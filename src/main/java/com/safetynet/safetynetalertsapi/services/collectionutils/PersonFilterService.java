package com.safetynet.safetynetalertsapi.services.collectionutils;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.Identifiable;
import com.safetynet.safetynetalertsapi.model.Identity;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.services.finders.MedicalRecordFinder;

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
	 * @param persons a list of person to be filtered ; any type that implements {@link Identifiable}.
	 * @return a List of persons classified as children. 
	 */
	public <T extends Identifiable> List<T> filterChildren(List<T> persons) {

		return persons.stream().filter(p -> {
			Identity identity = p.getIdentity();
			MedicalRecord record = recordFinder.findByIdentity(identity);

			return record != null && record.getBirthDate().isAfter(getLimitDateForChildren());
		}).toList();
	}

	/**
	 * Counts the number of adults (older than 18) in a given list of persons. 
	 * 
	 * @param persons a list of person to be filtered ; any type that implements {@link Identifiable}.
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
