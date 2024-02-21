package edu.hogwarts.studentadmin.dto;

import edu.hogwarts.studentadmin.model.EmpType;

import java.time.LocalDate;

public class TeacherDTO extends HogwartsPersonDTO{
    private Boolean headOfHouse;
    private EmpType employment;
    private LocalDate employmentStart;
    private LocalDate employmentEnd;

    public TeacherDTO() {
    }

    public TeacherDTO(Long id, String firstName, String middleName, String lastName, LocalDate dateOfBirth, String house, Boolean headOfHouse, EmpType employment, LocalDate employmentStart, LocalDate employmentEnd) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.houseName = house;
        this.headOfHouse = headOfHouse;
        this.employment = employment;
        this.employmentStart = employmentStart;
        this.employmentEnd = employmentEnd;
    }

    public Boolean isHeadOfHouse() {
        return headOfHouse;
    }

    public void setHeadOfHouse(Boolean headOfHouse) {
        this.headOfHouse = headOfHouse;
    }

    public EmpType getEmployment() {
        return employment;
    }

    public void setEmployment(EmpType employment) {
        this.employment = employment;
    }

    public LocalDate getEmploymentStart() {
        return employmentStart;
    }

    public void setEmploymentStart(LocalDate employmentStart) {
        this.employmentStart = employmentStart;
    }

    public LocalDate getEmploymentEnd() {
        return employmentEnd;
    }

    public void setEmploymentEnd(LocalDate employmentEnd) {
        this.employmentEnd = employmentEnd;
    }
}
