/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.education;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author xmasari1
 */
public class SchoolManagerImplTest {

    private SchoolManager manager;
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = prepareDataSource();
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("CREATE TABLE SCHOOL ("
                    + "id bigint primary key generated always as identity,"
                    + "nam varchar(255),"
                    + "address varchar(255),").executeUpdate();
        } catch (SQLException e) {
            throw new AttendanceFailureException("Error when creating table ", e);
        }
        manager = new SchoolManagerImpl(dataSource);
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
        School school = newSchool("Gates", "Hell");
        manager.createSchool(school);
        Long studentId = student.getId();
        assertThat("saved student has null id", student.getId(), is(not(equalTo(null))));
        assertNotNull("saved student has null id", studentId);
        Student result = manager.findSschoolById(schoolId);
        assertThat("loaded student differs from the saved one", result, is(equalTo(student)));
        assertThat("loaded grave is the same instance", result, is(not(sameInstance(student))));
        assertEquals(student, result);
    }

    @Test
    public void testUpdateSchool() throws Exception {
        School school = newSchool("Heaven","Up");
        School school1 = newSchool("Hell","Down in the fucking ground");
        manager.createSchool(school);
        manager.createSchool(school1);
        school.setAddress("Up there");
        manager.updateSchool(school);
        assertEquals("Heaven", manager.findSchoolById(school.getId()).getName());
        assertEquals("Up there",school.getAddress());

    }

    @Test
    public void testDeleteSchool() throws Exception {
        School school = newSchool("Heaven","Up there");
        School school1 = newSchool("Hell","Down in the fucking ground");
        School school2 = newSchool("translate:Ocistec","In the fuckin middle");
        manager.createSchool(school);
        manager.createSchool(school1);
        manager.createSchool(school2);
        Long schoolId = school.getId();
        manager.deleteSchool(school);
        assertNull(manager.findSchoolById(schoolId));
        assertNotNull(manager.findSchoolById(school2.getId()));

    }

    @Test
    public void testFindSchoolById() throws Exception {
        School school1 = newSchool("Hell","Down in the fucking ground");
        manager.createSchool(school1);
        School school = manager.findSchoolById(school1.getId());
        assertNotNull("School wasn't find", school1);
        assertEquals("Hell", school.getName());
    }

    @Test
    public void testFindAllSchools() throws Exception {
        School school = newSchool("Heaven","Up there");
        School school1 = newSchool("Hell","Down in the fucking ground");
        School school2 = newSchool("translate:Ocistec","In the fuckin middle");
        manager.createSchool(school);
        manager.createSchool(school1);
        manager.createSchool(school2);
        List<School> schools = manager.findAllSchools();
        assertNotNull("List is empty", schools);
        assertEquals(3, schools.size());
    }

    public static School newSchool(String name, String adr) {
        School school = new School();
        school.setName(name);
        school.setAddress(adr);
        
        return school;
    }
}
