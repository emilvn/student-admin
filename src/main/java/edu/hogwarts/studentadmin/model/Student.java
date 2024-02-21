package edu.hogwarts.studentadmin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This entity class represents a student at Hogwarts.
 */
@Entity(name = "student")
public class Student extends HogwartsPerson {
    private Boolean prefect;
    private Integer enrollmentYear;
    private Integer graduationYear;
    private Boolean graduated;

    private Integer schoolYear;
    @ManyToMany(mappedBy = "students")
    private List<Course> courses = new ArrayList<>();

    public Student() {

    }

    public Student(Long id, String firstName, String middleName, String lastName, LocalDate dateOfBirth, House house, boolean prefect, Integer enrollmentYear, Integer graduationYear, Boolean graduated, Integer schoolYear) {
        this.schoolYear = schoolYear;
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.house = house;
        this.prefect = prefect;
        this.enrollmentYear = enrollmentYear;
        this.graduationYear = graduationYear;
        this.graduated = graduated;
    }

    /**
     * Makes sure that the student is removed from all courses when they are deleted.
     */
    @PreRemove
    public void removeStudentFromCourse() {
        for (Course course : courses) {
            course.removeStudent(this);
        }
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Boolean isPrefect() {
        return prefect;
    }

    public void setPrefect(Boolean prefect) {
        this.prefect = prefect;
    }

    public Integer getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(Integer enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        if (graduationYear != null) {
            setGraduated(true);
        }
        this.graduationYear = graduationYear;
    }

    public Boolean isGraduated() {
        return graduated;
    }

    public void setGraduated(Boolean graduated) {
        this.graduated = graduated;
    }

    public Integer getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(Integer schoolYear) {
        this.schoolYear = schoolYear;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", house=" + house +
                ", prefect=" + prefect +
                ", schoolYear=" + schoolYear +
                ", enrollmentYear=" + enrollmentYear +
                ", graduationYear=" + graduationYear +
                ", graduated=" + graduated +
                '}';
    }
}
