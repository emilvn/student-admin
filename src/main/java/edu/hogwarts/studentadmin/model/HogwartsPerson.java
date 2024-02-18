package edu.hogwarts.studentadmin.model;

import com.fasterxml.jackson.annotation.JsonGetter;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    @JsonGetter("age")
    public int getAge() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now().withYear(1992)).getYears();
    }

    @JsonGetter("fullName")
    public String getFullName() {
        return firstName + " " + middleName + " " + lastName;
    }
}
