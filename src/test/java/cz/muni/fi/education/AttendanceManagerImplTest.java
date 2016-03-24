package cz.muni.fi.education;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by John on 13-Mar-16.
 */
public class AttendanceManagerImplTest {

    private AttendanceManager manager;

    @Before
    public void setUp() throws Exception {
        manager = new AttendanceManagerImpl();
    }
    /*
    @Test
    public void testAddStudentToSchool() throws Exception {
        School school = newSchool("Stanford","Great Britain, London, 68");
        Student student = newStudent("Bill Gates","1980-09-15");
        manager.addStudentToSchool(student,school);
        List<Student> students = manager.findStudentsInSchool(school);
        assertNotNull(manager.findSchoolByStudent(student));
        assertNotNull(students);
        assertNotEquals(0,students.size());

    }

    @Test
    public void testRemoveStudentFromSchool() throws Exception {
        School school = newSchool("Stanford","Great Britain, London, 68");
        Student student = newStudent("Bill Gates","1980-09-15");
        manager.addStudentToSchool(student,school);
        manager.removeStudentFromSchool(student,school);
        assertNull(manager.findSchoolByStudent(student));
    }

    @Test
    public void testFindStudentsInSchool() throws Exception {
        School school = newSchool("Stanford","Great Britain, London, 68");
        Student student = newStudent("Bill Gates","1980-09-15");
        manager.addStudentToSchool(student,school);
        List<Student> students = manager.findStudentsInSchool(school);
        assertNotNull(students);
        assertNotEquals(0,students.size());
    }

    @Test
    public void testFindSchoolByStudent() throws Exception {
        Student student = newStudent("Bill Gates","1980-09-15");
        School school = manager.findSchoolByStudent(student);
        assertNull(school);
    }

    private static Student newStudent(String name, String start){
        Student student = new Student();
        student.setName(name);
        try {
            student.setStart((new SimpleDateFormat("dd-MM-yyyy")).parse(start));
        } catch(ParseException e){
            throw new AssertionError("wrong parse start date",e);
        }
        return student;
    }

    private static School newSchool(String name,String address){
        School school = new School();
        school.setName(name);
        school.setAddress(address);
        return school;
    }
    */

}