package cz.muni.fi.education;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Created by John on 09-Mar-16.
 */
public class AttendanceManagerImpl implements AttendanceManager {

    private final DataSource dataSource;

    public AttendanceManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void checkSS(Student st, School sch) {
        checkSchool(sch);
        checkStudent(st);
    }

    public void checkSchool(School sch) {
        if (sch == null) {
            throw new IllegalArgumentException("school is null");
        }
        if (sch.getId() == null) {
            throw new IllegalArgumentException("school id is null");
        }
    }

    public void checkStudent(Student st) {
        if (st == null) {
            throw new IllegalArgumentException("student is null");
        }
        if (st.getId() == null) {
            throw new IllegalArgumentException("student id is null");
        }
    }

    @Override
    public void addStudentToSchool(Student student, School school) {
        checkSS(student, school);
        try (Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "INSERT INTO ATTENDANCE (schoolID,studentID) VALUES (?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            st.setLong(1, school.getId());
            st.setLong(2, student.getId());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new AttendanceFailureException("Internal Error: More rows ("
                        + addedRows + ") inserted when trying to insert school " + school);
            }

        } catch (SQLException ex) {
            throw new AttendanceFailureException("Error when inserting atttend " + school + student, ex);
        }
    }

    @Override
    public void removeStudentFromSchool(Student student, School school) {
        checkSS(student, school);
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "DELETE FROM ATTENDANCE WHERE schoolID = ? AND studentID = ?")) {

            st.setLong(1, student.getId());
            st.setLong(2, school.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("School " + school + "with student" + student + " was not found in database!");
            } else if (count != 1) {
                throw new AttendanceFailureException("Invalid deleted rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new AttendanceFailureException(
                    "Error when updating attendance " + school + student, ex);
        }
    }

    @Override
    public List<Student> findStudentsInSchool(School school) {
        checkSchool(school);
        SchoolManagerImpl.validateSchool(school);
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT * FROM STUDENT "
                                + "INNER JOIN ("
                                + "SELECT ATTENDANCE "
                                + "ON STUDNET.id = ATTENDANCE.studentID")) { // TODO

            st.setLong(1, school.getId());
            ResultSet rs = st.executeQuery();

            List<Student> result = new ArrayList<>();
            while (rs.next()) {
                result.add(StudentManagerImpl.resultSetToStudent(rs)); // TODO
            }
            return result;

        } catch (SQLException ex) {
            throw new AttendanceFailureException(
                    "Error when retrieving school with id ", ex);
        }
    }

    @Override
    public School findSchoolByStudent(Student student) {
        StudentManagerImpl.validateStudent(student);
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT * FROM SCHOOL WHERE id = SELECT schoolID FROM STUDENT WHERE studentID = ?")) { // check

            st.setLong(1, student.getId());
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                School school = SchoolManagerImpl.resultSetToSchool(rs); // TODO

                if (rs.next()) {
                    throw new AttendanceFailureException(
                            "Internal error: More entities with the same id found ");
                }

                return school;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            throw new AttendanceFailureException(
                    "Error when retrieving student with id ", ex);
        }
    }
}
