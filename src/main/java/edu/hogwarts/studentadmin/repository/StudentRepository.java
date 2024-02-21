package edu.hogwarts.studentadmin.repository;

import edu.hogwarts.studentadmin.model.Student;

import java.util.Optional;

/**
 * Repository for Student entities.
 */
public interface StudentRepository extends HogwartsPersonRepository<Student> {

    /**
     * Find the first student with the given first name, ignoring case.
     * @param firstName the first name to search for
     * @return the first student with the given first name, or an empty Optional if none is found
     */
    Optional<Student> findFirstByFirstNameIgnoreCase(String firstName);

    /**
     * Find the first student with the given first and last names, ignoring case.
     * @param firstName the first name to search for
     * @param lastName the last name to search for
     * @return the first student with the given first and last names, or an empty Optional if none is found
     */
    Optional<Student> findFirstByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    /**
     * Find the first student with the given first, middle, and last names, ignoring case.
     * @param firstName the first name to search for
     * @param middleName the middle name to search for
     * @param lastName the last name to search for
     * @return the first student with the given first, middle, and last names, or an empty Optional if none is found
     */
    Optional<Student> findFirstByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String middleName, String lastName);
}
