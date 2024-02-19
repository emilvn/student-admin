package edu.hogwarts.studentadmin.config;

import edu.hogwarts.studentadmin.model.EmpType;
import edu.hogwarts.studentadmin.model.House;
import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.HouseRepository;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import edu.hogwarts.studentadmin.repository.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {
    private final HouseRepository houseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public DataLoader(HouseRepository houseRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.houseRepository = houseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createHouses();
        createStudents();
        createTeachers();


    }

    private void createHouses() {
        var gryffindor = new House("Gryffindor", "Godric Gryffindor", "scarlet", "gold");
        var hufflepuff = new House("Hufflepuff", "Helga Hufflepuff", "yellow", "black");
        var ravenclaw = new House("Ravenclaw", "Rowena Ravenclaw", "blue", "silver");
        var slytherin = new House("Slytherin", "Salazar Slytherin", "green", "silver");


        houseRepository.save(gryffindor);
        houseRepository.save(hufflepuff);
        houseRepository.save(ravenclaw);
        houseRepository.save(slytherin);
    }

    private void createStudents() {
        var gryffindor = houseRepository.findById("Gryffindor");
        var hufflepuff = houseRepository.findById("Hufflepuff");
        var ravenclaw = houseRepository.findById("Ravenclaw");
        var slytherin = houseRepository.findById("Slytherin");
        if (gryffindor.isEmpty() || hufflepuff.isEmpty() || ravenclaw.isEmpty() || slytherin.isEmpty()) {
            return;
        }
        var harry = new Student(1L, "Harry", "James", "Potter", LocalDate.of(1980, 7, 31), gryffindor.get(), false, 1991, 1998, true);
        var hermione = new Student(2L, "Hermione", "Jean", "Granger", LocalDate.of(1979, 9, 19), gryffindor.get(), true, 1991, 1998, true);
        var ron = new Student(3L, "Ronald", "Bilius", "Weasley", LocalDate.of(1980, 3, 1), gryffindor.get(), false, 1991, 1998, true);
        var neville = new Student(4L, "Neville", "Frank", "Longbottom", LocalDate.of(1980, 7, 30), gryffindor.get(), false, 1991, 1998, true);
        var luna = new Student(5L, "Luna", "", "Lovegood", LocalDate.of(1981, 2, 13), ravenclaw.get(), false, 1992, 1999, true);
        var draco = new Student(6L, "Draco", "Lucius", "Malfoy", LocalDate.of(1980, 6, 5), slytherin.get(), false, 1991, 1998, true);
        var cedric = new Student(7L, "Cedric", "", "Diggory", LocalDate.of(1977, 9, 1), hufflepuff.get(), true, 1993, 1995, true);
        var cho = new Student(8L, "Cho", "", "Chang", LocalDate.of(1979, 9, 14), ravenclaw.get(), false, 1992, 1999, true);
        var ginny = new Student(9L, "Ginevra", "Molly", "Weasley", LocalDate.of(1981, 8, 11), gryffindor.get(), false, 1992, 1999, true);
        var seamus = new Student(10L, "Seamus", "", "Finnigan", LocalDate.of(1980, 3, 1), gryffindor.get(), false, 1991, 1998, true);
        var dean = new Student(11L, "Dean", "", "Thomas", LocalDate.of(1980, 1, 1), gryffindor.get(), false, 1991, 1998, true);
        var parvati = new Student(12L, "Parvati", "", "Patil", LocalDate.of(1980, 1, 1), gryffindor.get(), false, 1991, 1998, true);

        var students = new Student[]{harry, hermione, ron, neville, luna, draco, cedric, cho, ginny, seamus, dean, parvati};
        studentRepository.saveAll(Arrays.asList(students));
    }

    private void createTeachers() {
        var gryffindor = houseRepository.findById("Gryffindor");
        var hufflepuff = houseRepository.findById("Hufflepuff");
        var ravenclaw = houseRepository.findById("Ravenclaw");
        var slytherin = houseRepository.findById("Slytherin");
        if (gryffindor.isEmpty() || hufflepuff.isEmpty() || ravenclaw.isEmpty() || slytherin.isEmpty()) {
            return;
        }
        var mcGonagall = new Teacher(1L, "Minerva", "", "McGonagall", LocalDate.of(1935, 10, 4), gryffindor.get(), true, EmpType.TENURED, LocalDate.of(1956, 9, 1), null);
        var snape = new Teacher(2L, "Severus", "", "Snape", LocalDate.of(1960, 1, 9), slytherin.get(), true, EmpType.TENURED, LocalDate.of(1981, 9, 1), LocalDate.of(1998, 6, 30));
        var sprout = new Teacher(3L, "Pomona", "", "Sprout", LocalDate.of(1931, 5, 15), hufflepuff.get(), true, EmpType.TENURED, LocalDate.of(1952, 9, 1), null);
        var flitwick = new Teacher(4L, "Filius", "", "Flitwick", LocalDate.of(1930, 10, 17), ravenclaw.get(), true, EmpType.TENURED, LocalDate.of(1951, 9, 1), null);
        var hagrid = new Teacher(5L, "Rubeus", "", "Hagrid", LocalDate.of(1928, 12, 6), gryffindor.get(), false, EmpType.TENURED, LocalDate.of(1968, 9, 1), LocalDate.of(1998, 6, 30));
        var trelawney = new Teacher(6L, "Sybill", "", "Trelawney", LocalDate.of(1963, 3, 9), ravenclaw.get(), false, EmpType.TENURED, LocalDate.of(1993, 9, 1), null);
        var binns = new Teacher(7L, "Cuthbert", "", "Binns", LocalDate.of(1865, 1, 1), null, false, EmpType.TENURED, LocalDate.of(1886, 9, 1), LocalDate.of(1986, 6, 30));
        var quirrell = new Teacher(8L, "Quirinus", "", "Quirrell", LocalDate.of(1968, 9, 26), ravenclaw.get(), false, EmpType.TEMPORARY, LocalDate.of(1991, 9, 1), LocalDate.of(1992, 6, 30));

        var teachers = new Teacher[]{mcGonagall, snape, sprout, flitwick, hagrid, trelawney, binns, quirrell};
        teacherRepository.saveAll(Arrays.asList(teachers));
    }
}
