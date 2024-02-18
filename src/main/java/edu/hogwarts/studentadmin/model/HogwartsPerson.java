package edu.hogwarts.studentadmin.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@MappedSuperclass
public abstract class HogwartsPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String firstName;
    protected String middleName;
    protected String lastName;
    protected LocalDate dateOfBirth;
    protected @ManyToOne(fetch = FetchType.EAGER) House house;

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

    @JsonGetter("fullName")
    public String getFullName() {
        String fullName = firstName;

        if (middleName != null) {
            fullName += " " + middleName;
        }
        if(lastName != null) {
            fullName += " " + lastName;
        }
        return fullName;
    }

    @JsonSetter("fullName")
    public void setFullName(String fullName) {
        int firstSpace = fullName.indexOf(' ');
        int lastSpace = fullName.lastIndexOf(' ');

        if(firstSpace == -1) {
            setFirstName(fullName);
            setMiddleName(null);
            setLastName(null);
        }
        else if (firstSpace == lastSpace) {
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

    @JsonGetter("age")
    public int getAge() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now().withYear(1992)).getYears();
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
