package com.safetynet.safetynetalertsapi.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.safetynet.safetynetalertsapi.model.Identity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MedicalRecordDTO {

    public MedicalRecordDTO() {
    }

    public MedicalRecordDTO(Identity identity, LocalDate birthDate, List<String> allergies, List<String> medications) {
        this.identity = identity;
        this.birthDate = birthDate;
        this.allergies = allergies;
        this.medications = medications;
    }

    @JsonUnwrapped
    private Identity identity;

    @JsonProperty(value = "birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private LocalDate birthDate;

    @JsonProperty("allergies")
    private List<String> allergies;

    @JsonProperty("medications")
    private List<String> medications;

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    @Override
    public String toString() {
        return this.identity.toString() + " " + this.birthDate + " " + this.allergies + " " + this.medications;
    }
}
