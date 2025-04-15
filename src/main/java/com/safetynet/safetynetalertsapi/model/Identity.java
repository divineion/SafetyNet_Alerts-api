package com.safetynet.safetynetalertsapi.model;

import java.util.Objects;

public class Identity {
    private String firstName;

    private String lastName;

    public Identity() {
    }

    public Identity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return this.firstName + this.lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identity identity = (Identity) o;
        return Objects.equals(firstName, ((Identity) o).getFirstName()) && Objects.equals(lastName, ((Identity) o).getLastName());
    }
}
