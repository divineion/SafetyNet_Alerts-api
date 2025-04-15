package com.safetynet.safetynetalertsapi.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents the medical record of a {@link Person} person in the SafetyNet Alerts system.
 * This class contains personal medical information such as the person's name, birth date,
 * allergies, and medications.
 */
public class MedicalRecord implements Identifiable {
    @JsonUnwrapped
    private Identity identity;

    @JsonProperty(value = "birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private LocalDate birthDate;

    private List<String> allergies;

    private List<String> medications;

    public MedicalRecord() {
    }

    public MedicalRecord(Identity identity, LocalDate birthDate, List<String> allergies, List<String> medications) {
        this.identity = identity;
        this.birthDate = birthDate;
        this.allergies = allergies;
        this.medications = medications;
    }

    public Identity getIdentity() {
        return identity;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public List<String> getMedications() {
        return medications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MedicalRecord that = (MedicalRecord) o;
        return Objects.equals(identity, that.identity)
                && Objects.equals(birthDate, that.birthDate)
                && Objects.equals(allergies, that.allergies)
                && Objects.equals(medications, that.medications);
    }
}
