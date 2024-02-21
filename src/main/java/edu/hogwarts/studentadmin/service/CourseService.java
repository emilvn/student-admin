package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.dto.CourseDTO;
import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.dto.TeacherDTO;
import edu.hogwarts.studentadmin.model.Course;
import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * This class provides service methods to manage courses in the school.
 */
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;

    /**
     * Constructor for CourseService. Uses dependency injection to get the CourseRepository, TeacherService, and StudentService.
     * @param courseRepository The repository for courses
     * @param teacherService The service for teachers
     * @param studentService The service for students
     */
    public CourseService(CourseRepository courseRepository, TeacherService teacherService, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    /**
     * Converts a course entity to a course DTO.
     * If the teacher or students are not null, they are fetched from the database to ensure they exist.
     * @param course The course entity to convert.
     * @return The course DTO.
     */
    public CourseDTO convertToDTO(Course course) {
        var courseDTO = new CourseDTO();
        if(course.getId() != null) {
            courseDTO.setId(course.getId());
        }
        courseDTO.setSubject(course.getSubject());
        courseDTO.setSchoolYear(course.getSchoolYear());
        courseDTO.setCurrent(course.isCurrent());
        if (course.getTeacher() != null) {
            courseDTO.setTeacher(teacherService.convertToDTO(course.getTeacher()));
        }
        if (course.getStudents() != null) {
            var studentDTOs = course.getStudents().stream().map(studentService::convertToDTO).toList();
            if(studentDTOs.stream().allMatch(Objects::nonNull)) {
                courseDTO.setStudents(studentDTOs);
            }
        }
        return courseDTO;
    }

    /**
     * Converts a course DTO to a course entity.
     * If the teacher or students are not null, they are fetched from the database to ensure they exist.
     * @param courseDTO The course DTO to convert.
     * @return The course entity.
     */
    public Course convertToEntity(CourseDTO courseDTO) {
        var courseEntity = new Course();
        if(courseDTO.getId() != null){
            courseEntity.setId(courseDTO.getId());
        }
        courseEntity.setSubject(courseDTO.getSubject());
        courseEntity.setSchoolYear(courseDTO.getSchoolYear());
        courseEntity.setCurrent(courseDTO.isCurrent());
        if (courseDTO.getTeacher() != null) {
            courseEntity.setTeacher(teacherService.getEntity(courseDTO.getTeacher().getId()));
        }
        if (courseDTO.getStudents() != null) {
            var studentEntities = courseDTO.getStudents().stream().map(s-> studentService.getEntity(s.getId())).toList();
            if(studentEntities.stream().allMatch(Objects::nonNull)) {
                courseEntity.setStudents(studentEntities);
            }
        }
        return courseEntity;
    }

    /**
     * Gets list of all courses from the database
     * @return List of all courses
     */
    public List<CourseDTO> getAll() {
        var courses = courseRepository.findAll();
        return courses.stream().map(this::convertToDTO).toList();
    }

    /**
     * Gets a specific course by its id
     * @param id The id of the course
     * @return The course with the given id, or null if it doesn't exist
     */
    public CourseDTO get(Long id) {
        var course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            return null;
        }
        return convertToDTO(course);
    }

    /**
     * Creates a new course.
     * @param courseDTO The course to create
     * @return The created course
     */
    public CourseDTO create(CourseDTO courseDTO) {
        var courseEntity = convertToEntity(courseDTO);
        return convertToDTO(courseRepository.save(courseEntity));
    }

    /**
     * Updates a course overwriting all its fields with the new course data.
     * @param id The id of the course to update
     * @param courseDTO The new course data
     * @return The updated course, or null if the course doesn't exist
     */
    public CourseDTO update(Long id, CourseDTO courseDTO) {
        var courseEntity = courseRepository.findById(id).orElse(null);
        if (courseEntity == null) {
            return null;
        }
        courseEntity = convertToEntity(courseDTO);
        return convertToDTO(courseRepository.save(courseEntity));
    }

    /**
     * Updates a course overwriting only the fields that are not null in the new course data.
     * If the teacher or students are not null, they are fetched from the database to ensure they exist.
     * @param id The id of the course to update
     * @param courseDTO The new course data
     * @return The updated course, or null if the course doesn't exist
     */
    public CourseDTO patch(Long id, CourseDTO courseDTO) {
        var courseEntity = courseRepository.findById(id).orElse(null);
        if (courseEntity == null) {
            return null;
        }

        if (courseDTO.getSubject() != null) {
            courseEntity.setSubject(courseDTO.getSubject());
        }

        if (courseDTO.getTeacher() != null) {
            var teacher = teacherService.getEntity(courseDTO.getTeacher().getId());
            courseEntity.setTeacher(teacher);
        }

        if (courseDTO.getStudents() != null) {
            var studentEntities = courseDTO.getStudents()
                    .stream()
                    .map(student -> studentService.getEntity(student.getId()))
                    .toList();
            courseEntity.setStudents(studentEntities);
        }

        if (courseDTO.getSchoolYear() != null) {
            courseEntity.setSchoolYear(courseDTO.getSchoolYear());
        }

        if (courseDTO.isCurrent() != null) {
            courseEntity.setCurrent(courseDTO.isCurrent());
        }

        return convertToDTO(courseRepository.save(courseEntity));
    }

    /**
     * Updates the teacher of a course.
     * If the teacher is not null, it is fetched from the database to ensure it exists.
     * @param id The id of the course to update
     * @param teacherDTO The new teacher
     * @return The updated course, or null if the course doesn't exist or the teacher doesn't exist
     */
    public CourseDTO updateTeacher(Long id, TeacherDTO teacherDTO) {
        var courseEntity = courseRepository.findById(id).orElse(null);
        if (courseEntity == null) {
            return null;
        }

        Teacher teacherEntity;
        if(teacherDTO != null){
            teacherEntity = teacherService.getEntity(teacherDTO.getId());
        } else {
            teacherEntity = null;
        }

        courseEntity.setTeacher(teacherEntity);
        return convertToDTO(courseRepository.save(courseEntity));
    }

    /**
     * Adds students to a course, finding them by their names.
     * If the students are not null, they are fetched from the database to ensure they exist.
     * @param id The id of the course to update
     * @param studentDTOS The new students as a list of Student objects with at least their names set
     * @return The updated course, or null if the course doesn't exist
     */
    public CourseDTO addStudentsByName(Long id, List<StudentDTO> studentDTOS) {
        var courseEntity = courseRepository.findById(id).orElse(null);
        if (courseEntity == null) {
            return null;
        }

        var studentEntities = studentDTOS
                .stream()
                .map(student -> studentService.get(student.getName()))
                .toList();
        if(studentEntities.stream().allMatch(Objects::nonNull)){
            courseEntity.getStudents().addAll(studentEntities);
        }
        return convertToDTO(courseRepository.save(courseEntity));
    }

    /**
     * Adds students to a course, finding them by their ids.
     * If the students are not null, they are fetched from the database to ensure they exist.
     * @param id The id of the course to update
     * @param studentDTOS The new students as a list of Student objects with at least their ids set
     * @return The updated course, or null if the course doesn't exist
     */
    public CourseDTO addStudentsById(Long id, List<StudentDTO> studentDTOS) {
        var courseEntity = courseRepository.findById(id).orElse(null);
        if (courseEntity == null) {
            return null;
        }

        if(studentDTOS.isEmpty()){
            return convertToDTO(courseEntity);
        }

        var studentEntities = studentDTOS
                .stream()
                .map(student -> studentService.getEntity(student.getId()))
                .toList();
        if(studentEntities.stream().allMatch(Objects::nonNull)){
            courseEntity.getStudents().addAll(studentEntities);
        }
        return convertToDTO(courseRepository.save(courseEntity));
    }

    /**
     * Deletes a course by its id
     * @param id The id of the course to delete
     */
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    /**
     * Removes the teacher from a course, setting it to null.
     * @param id The id of the course to update
     */
    public void removeTeacher(Long id) {
        var course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            return;
        }
        course.setTeacher(null);
        courseRepository.save(course);
    }

    /**
     * Removes a specific student from a course.
     * @param id The id of the course to update
     * @param studentId The id of the student to remove
     */
    public void removeStudent(Long id, Long studentId) {
        var course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            return;
        }
        var students = course.getStudents();
        var student = studentService.getEntity(studentId);
        if (!students.contains(student)) {
            return;
        }
        course.getStudents().remove(student);
        courseRepository.save(course);
    }

}
