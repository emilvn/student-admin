package edu.hogwarts.studentadmin.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import edu.hogwarts.studentadmin.model.House;

import java.time.LocalDate;
import java.time.Period;

/**
 * This is a DTO superclass for HogwartsPerson entities.
 */
public class HogwartsPersonDTO {
    protected Long id;
    protected String firstName;
    protected String middleName;
    protected String lastName;
    protected LocalDate dateOfBirth;
    protected String houseName;
    protected House house;


    public HogwartsPersonDTO() {
    }

    public HogwartsPersonDTO(Long id, String firstName, String middleName, String lastName, LocalDate dateOfBirth, String houseName) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.houseName = houseName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @JsonIgnore
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @JsonIgnore
    public String getLastName() {
        return lastName;
    }

    @JsonIgnore
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonIgnore
    public String getHouseName() {
        return houseName;
    }

    @JsonSetter("house")
    public void setHouseName(String houseName) {
        char firstLetter = houseName.charAt(0);
        houseName = Character.toUpperCase(firstLetter) + houseName.substring(1);
        this.houseName = houseName;
    }

    @JsonIgnore
    public House getHouse() {
        return house;
    }

    @JsonIgnore
    public void setHouse(House house) {
        this.house = house;
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

    @JsonGetter("house")
    public String getHouseJson() {
        if(house == null) return null;
        return house.getName();
    }

}
