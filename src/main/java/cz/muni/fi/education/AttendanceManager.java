package cz.muni.fi.education;

import java.util.List;

/**
 * Created by John on 09-Mar-16.
 */
public interface AttendanceManager {

    /**
     * Method add student to specified school and set student parameter start
     * It join student entity with school entity
     *
     * @param student student who will be added
     * @param school school where student will be added
     */
    void addStudentToSchool(Student student,School school);
    
    /**
     * Method remove student from specified school
     * It set student parameter end of school
     *
     * @param student student who will be removed
     * @param school school where student will be removed
     */
    void removeStudentFromSchool(Student student,School school);

    /**
     * Method find all student in specified school and return them
     * Student must still be in school
     *
     * @param school school with students to return
     * @return list of students in school
     */
    List<Student> findStudentsInSchool(School school);

    /**
     * Method find school where visits specified student
     *
     * @param student student who visits school to find
     * @return school where visit student
     */
    School findSchoolByStudent(Student student);
}
