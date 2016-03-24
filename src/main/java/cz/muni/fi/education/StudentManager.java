package cz.muni.fi.education;

import java.util.List;

/**
 * Created by John on 09-Mar-16.
 */
public interface StudentManager {

    void createStudent(Student student);

    void updateStudent(Student student);

    void deleteStudent(Student student);

    Student findStudentById(long id);

    List<Student> findAllStudents();
}
