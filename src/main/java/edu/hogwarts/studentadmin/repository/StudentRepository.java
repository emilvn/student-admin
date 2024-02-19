package edu.hogwarts.studentadmin.repository;

import edu.hogwarts.studentadmin.model.Student;

import java.util.Optional;

public interface StudentRepository extends HogwartsPersonRepository<Student> {

    Optional<Student> findFirstByFirstNameIgnoreCase(String firstName);
    Optional<Student> findFirstByMiddleNameIgnoreCase(String middleName);
    Optional<Student> findFirstByLastNameIgnoreCase(String lastName);

    Optional<Student> findFirstByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    Optional<Student> findFirstByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String middleName, String lastName);
}
