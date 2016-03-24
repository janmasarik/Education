package cz.muni.fi.education;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 09-Mar-16.
 */
public class StudentManagerImpl implements StudentManager {

    private final DataSource dataSource;

    public StudentManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createStudent(Student student) throws AttendanceFailureException{
        validateStudent(student);
        if (student.getId() != null) {
            throw new IllegalArgumentException("student id is already set");
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "INSERT INTO STUDENT (nam,start,ended) VALUES (?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, student.getName());
            st.setDate(2, student.getStart());
            st.setDate(3, student.getEnd());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new AttendanceFailureException("Internal Error: More rows ("
                        + addedRows + ") inserted when trying to insert student " + student);
            }
            ResultSet keyRS = st.getGeneratedKeys();
            student.setId(getKey(keyRS, student));
        } catch (SQLException ex) {
            throw new AttendanceFailureException("Error when inserting student " + student, ex);
        }
    }

    public static void validateStudent(Student student)throws IllegalArgumentException {
        if (student == null) {
            throw new IllegalArgumentException("student is null");
        }
        if (student.getName() == null) {
            throw new IllegalArgumentException("student name is null");
        }
        if (student.getStart() == null) {
            throw new IllegalArgumentException("student start is null");
        }
        if (student.getEnd() == null) {
            throw new IllegalArgumentException("student end is null");
        }
    }

    private Long getKey(ResultSet keyRS, Student student) throws AttendanceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new AttendanceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert student " + student
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new AttendanceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert student " + student
                        + " - more keys found");
            }
            return result;
        } else {
            throw new AttendanceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert student " + student
                    + " - no key found");
        }
    }

    public static Student resultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getLong("id"));
        student.setName(rs.getString("nam"));
        student.setStart(rs.getDate("start"));
        student.setEnd(rs.getDate("ended"));
        return student;
    }

    public void updateStudent(Student student) throws AttendanceFailureException {
        validateStudent(student);
        if (student.getId() == null) {
            throw new IllegalArgumentException("student id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "UPDATE STUDENT SET nam = ?, start = ?, ended = ? WHERE id = ?")) {

            st.setString(1, student.getName());
            st.setDate(2, student.getStart());
            st.setDate(3, student.getEnd());
            st.setLong(4, student.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("Student " + student + " was not found in database!");
            } else if (count != 1) {
                throw new AttendanceFailureException("Invalid updated rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new AttendanceFailureException(
                    "Error when updating student " + student, ex);
        }
    }

    public void deleteStudent(Student student) throws AttendanceFailureException{
        if (student == null) {
            throw new IllegalArgumentException("student is null");
        }
        if (student.getId() == null) {
            throw new IllegalArgumentException("student id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "DELETE FROM STUDENT WHERE id = ?")) {

            st.setLong(1, student.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("Student " + student + " was not found in database!");
            } else if (count != 1) {
                throw new AttendanceFailureException("Invalid deleted rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new AttendanceFailureException(
                    "Error when updating student " + student, ex);
        }
    }

    public Student findStudentById(long id)  throws AttendanceFailureException{
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,nam,start,ended FROM STUDENT WHERE id = ?")) {

            st.setLong(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Student student = resultSetToStudent(rs);

                if (rs.next()) {
                    throw new AttendanceFailureException(
                            "Internal error: More entities with the same id found "
                                    + "(source id: " + id + ", found " + student + " and " + resultSetToStudent(rs));
                }

                return student;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            throw new AttendanceFailureException(
                    "Error when retrieving student with id " + id, ex);
        }
    }

    public List<Student> findAllStudents() {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,nam,start,ended FROM STUDENT")) {

            ResultSet rs = st.executeQuery();

            List<Student> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToStudent(rs));
            }
            return result;

        } catch (SQLException ex) {
            throw new AttendanceFailureException("Error when retrieving all students", ex);
        }
    }
}
