package cz.muni.fi.education;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by John on 12-Mar-16.
 */
public class StudentManagerImplTest {

    private StudentManager manager;
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = prepareDataSource();
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("CREATE TABLE STUDENT ("
                    + "id bigint primary key generated always as identity,"
                    + "nam varchar(255),"
                    + "start Date,"
                    + "ended Date)").executeUpdate();
        } catch (SQLException e) {
            throw new AttendanceFailureException("Error when create table ", e);
        }
         manager = new StudentManagerImpl(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("DROP TABLE STUDENT").executeUpdate();
        }
    }



    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:education");
        ds.setCreateDatabase("create");
        return ds;
    }

    @Test
    public void testCreateSchool() throws Exception {
        Student student = newStudent("Bill Gates","15-09-1980","20-05-1984");
        manager.createStudent(student);
        Long studentId = student.getId();
        assertThat("saved student has null id", student.getId(), is(not(equalTo(null))));
        assertNotNull("saved student has null id", studentId);
        Student result = manager.findStudentById(studentId);
        assertThat("loaded student differs from the saved one", result, is(equalTo(student)));
        assertThat("loaded grave is the same instance", result, is(not(sameInstance(student))));
        assertEquals(student, result);
    }

    @Test
    public void testUpdateSchool() throws Exception {
        Student student = newStudent("Bill Gates","15-09-1980","20-05-1984");
        manager.createStudent(student);
        student.setEnd((new SimpleDateFormat("dd-MM-yyyy")).parse("20-05-1984"));
        manager.updateStudent(student);
        assertNotNull("Student wasn't update",student.getEnd());
        assertEquals("Bill Gates",student.getName());
        assertEquals((new SimpleDateFormat("dd-MM-yyyy")).parse("20-05-1984"),student.getEnd());
        assertEquals((new SimpleDateFormat("dd-MM-yyyy")).parse("15-09-1980"),student.getStart());

    }

    @Test
    public void testDeleteSchool() throws Exception {
        Student student = newStudent("Bill Gates","15-09-1980","20-05-1984");
        Student student2 = newStudent("Steve Jobs","12-09-1982","20-05-1984");
        manager.createStudent(student);
        manager.createStudent(student2);
        Long studentId = student.getId();
        manager.deleteStudent(student);
        assertNull(manager.findStudentById(studentId));
        assertNotNull(manager.findStudentById(student2.getId()));

    }

    @Test
    public void testFindSchoolById() throws Exception {
        Student student = newStudent("Bill Gates","15-09-1980","20-05-1984");
        manager.createStudent(student);
        Student student2 = manager.findStudentById(student.getId());
        assertNotNull("Student wasn't find",student2);
        assertEquals("Bill Gates",student2.getName());
        assertEquals((new SimpleDateFormat("dd-MM-yyyy")).parse("15-09-1980"),student2.getStart());
    }

    @Test
    public void testFindAllSchools() throws Exception {
        Student student = newStudent("Bill Gates","15-09-1980","20-05-1984");
        Student student2 = newStudent("Steve Jobs","12-09-1982","20-05-1984");
        Student student3 = newStudent("Mark Zuckeberg","24-09-1995","20-05-1984");
        manager.createStudent(student);
        manager.createStudent(student2);
        manager.createStudent(student3);
        List<Student> students = manager.findAllStudents();
        assertNotNull("List is empty",students);
        assertEquals(3,students.size());
    }

    private static Student newStudent(String name, String start, String end){
        Student student = new Student();
        student.setName(name);
        try {
            student.setStart((new SimpleDateFormat("dd-MM-yyyy")).parse(start));
            student.setEnd((new SimpleDateFormat("dd-MM-yyyy")).parse(end));
        } catch(ParseException e){
            throw new AssertionError("wrong parse start date",e);
        }
        return student;
    }
}