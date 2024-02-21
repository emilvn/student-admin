package edu.hogwarts.studentadmin.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

/**
 * This superclass represents a person at Hogwarts.
 */
@MappedSuperclass
public abstract class HogwartsPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String firstName;
    protected String middleName;
    protected String lastName;
    protected LocalDate dateOfBirth;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "house")
    protected House house;

    /**
     * Transient field for the house name.
     * This field is not stored in the database,
     * but is used to store the house name when reading from the JSON file,
     * and later to set the house field by finding the house by name.
     */
    @Transient
    protected String houseName;

    @JsonIgnore
    public String getFirstName() {
        return firstName;
    }

    @JsonIgnore
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonIgnore
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @JsonIgnore
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * JSON getter for the full name of the person.
     * Used when serializing to the JSON file.
     * Used to get the full name as one field in the JSON file, rather than separate first, middle, and last names.
     * @return The full name of the person.
     */
    @JsonGetter("name")
    public String getName() {
        String fullName = firstName;

        if (middleName != null && !middleName.isBlank()) {
            fullName += " " + middleName;
        }
        if (lastName != null) {
            fullName += " " + lastName;
        }
        return fullName;
    }

    /**
     * JSON setter for the full name of the person.
     * Used when deserializing from the JSON file.
     * Makes it possible to set the full name of the person in the JSON file.
     * This method splits the full name into first, middle, and last names.
     * @param fullName The full name of the person.
     */
    @JsonSetter("name")
    public void setName(String fullName) {
        int firstSpace = fullName.indexOf(' ');
        int lastSpace = fullName.lastIndexOf(' ');

        if (firstSpace == -1) {
            setFirstName(fullName);
            setMiddleName(null);
            setLastName(null);
        } else if (firstSpace == lastSpace) {
            setFirstName(fullName.substring(0, firstSpace));
            setMiddleName(null);
            setLastName(fullName.substring(firstSpace + 1));
        } else {
            setFirstName(fullName.substring(0, firstSpace));
            setMiddleName(fullName.substring(firstSpace + 1, lastSpace));
            setLastName(fullName.substring(lastSpace + 1));
        }
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * JSON getter for the age of the person.
     * Used when serializing to the JSON file.
     * This method calculates the age of the person based on the date of birth.
     * @return The age of the person.
     */
    @JsonGetter("age")
    public int getAge() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now().withYear(1992)).getYears();
    }

    @JsonIgnore
    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    /**
     * JSON getter for the house name of the person.
     * Used when serializing to the JSON file.
     * Makes sure that only the name of the house is serialized to the JSON file, rather than the whole house object.
     * @return The house name of the person.
     */
    @JsonGetter("house")
    public String getHouseJson() {
        if (house == null) return null;
        return house.getName();
    }

    public String getHouseName() {
        return houseName;
    }

    /**
     * JSON setter for the house name of the person.
     * Used when deserializing from the JSON file.
     * This method sets the house name of the person based on the JSON file.
     * It capitalizes the first letter of the house name to match the database.
     * @param houseName The house name of the person.
     */
    @JsonSetter("house")
    public void setHouseName(String houseName) {
        char firstLetter = Character.toUpperCase(houseName.charAt(0));
        houseName = firstLetter + houseName.substring(1).toLowerCase();
        this.houseName = houseName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
