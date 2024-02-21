package edu.hogwarts.studentadmin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A teacher at Hogwarts

 */
@Entity(name = "teacher")
public class Teacher extends HogwartsPerson {
    private Boolean headOfHouse;

    @Enumerated(EnumType.STRING)
    private EmpType employment;
    private LocalDate employmentStart;
    private LocalDate employmentEnd;

    @OneToMany(mappedBy = "teacher")
    private List<Course> courses = new ArrayList<>();

    public Teacher() {
    }

    public Teacher(Long id, String firstName, String middleName, String lastName, LocalDate dateOfBirth, House house, boolean headOfHouse, EmpType employment, LocalDate employmentStart, LocalDate employmentEnd) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.house = house;
        this.headOfHouse = headOfHouse;
        this.employment = employment;
        this.employmentStart = employmentStart;
        this.employmentEnd = employmentEnd;
    }

    /**
     * Remove teacher from courses when teacher is removed
     */
    @PreRemove
    private void removeTeacherFromCourse() {
        for (Course course : courses) {
            course.setTeacher(null);
        }
    }

    @JsonIgnore
    public List<Course> getCourses() {
        return courses;
    }

    @JsonIgnore
    public void setCourses(List<Course> courses) {
        this.courses = courses;
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

    @Override
    public String toString() {
        return "Teacher{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", house=" + house +
                ", headOfHouse=" + headOfHouse +
                ", employment=" + employment +
                ", employmentStart=" + employmentStart +
                ", employmentEnd=" + employmentEnd +
                '}';
    }
}
