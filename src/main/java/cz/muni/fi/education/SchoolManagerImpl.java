package cz.muni.fi.education;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 09-Mar-16.
 */
public class SchoolManagerImpl implements SchoolManager {

    private final DataSource dataSource;

    public SchoolManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createSchool(School school) throws AttendanceFailureException {
        validateSchool(school); // prepusta exception, mala by byt asi hore
        if (school.getId() != null) {
            throw new IllegalArgumentException("school id is already set");
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                        "INSERT INTO SCHOOL (nam,address) VALUES (?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, school.getName());
            st.setString(2, school.getAddress());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new AttendanceFailureException("Internal Error: More rows ("
                        + addedRows + ") inserted when trying to insert school " + school);
            }

            ResultSet keyRS = st.getGeneratedKeys();
            school.setId(getKey(keyRS, school));

        } catch (SQLException ex) {
            throw new AttendanceFailureException("Error when inserting school " + school, ex);
        }
    }

    public static void validateSchool(School school) {
        if (school == null) {
            throw new IllegalArgumentException("school is null");
        }
        if (school.getName() == null) {
            throw new IllegalArgumentException("school name is null");
        }
        if (school.getAddress() == null) {
            throw new IllegalArgumentException("school address is null");
        }
    }

    private Long getKey(ResultSet keyRS, School school) throws AttendanceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new AttendanceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert school " + school
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new AttendanceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert school " + school
                        + " - more keys found");
            }
            return result;
        } else {
            throw new AttendanceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert school " + school
                    + " - no key found");
        }
    }

    public static School resultSetToSchool(ResultSet rs) throws SQLException {
        School school = new School();
        school.setId(rs.getLong("id"));
        school.setName(rs.getString("nam"));
        school.setAddress(rs.getString("address"));
        return school;
    }

    public void updateSchool(School school)throws AttendanceFailureException {
        validateSchool(school);
        if (school.getId() == null) {
            throw new IllegalArgumentException("school id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "UPDATE SCHOOL SET nam = ?, address = ? WHERE id = ?")) {

            st.setString(1, school.getName());
            st.setString(2, school.getAddress());
            st.setLong(3, school.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("School " + school + " was not found in database!");
            } else if (count != 1) {
                throw new AttendanceFailureException("Invalid updated rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new AttendanceFailureException(
                    "Error when updating school " + school, ex);
        }
    }

    public void deleteSchool(School school) throws AttendanceFailureException {
        if (school == null) {
            throw new IllegalArgumentException("school is null");
        }
        if (school.getId() == null) {
            throw new IllegalArgumentException("school id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "DELETE FROM SCHOOL WHERE id = ?")) {

            st.setLong(1, school.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("School " + school + " was not found in database!");
            } else if (count != 1) {
                throw new AttendanceFailureException("Invalid deleted rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new AttendanceFailureException(
                    "Error when updating school " + school, ex);
        }
    }

    public School findSchoolById(Long id) throws AttendanceFailureException{
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,nam,address FROM SCHOOL WHERE id = ?")) {

            st.setLong(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                 School school = resultSetToSchool(rs);

                if (rs.next()) {
                    throw new AttendanceFailureException(
                            "Internal error: More entities with the same id found "
                                    + "(source id: " + id + ", found " + school + " and " + resultSetToSchool(rs));
                }

                return school;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            throw new AttendanceFailureException(
                    "Error when retrieving school with id " + id, ex);
        }
    }

    public List<School> findAllSchools() throws AttendanceFailureException{
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,nam,address FROM SCHOOL")) {

            ResultSet rs = st.executeQuery();

            List<School> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToSchool(rs));
            }
            return result;

        } catch (SQLException ex) {
            throw new AttendanceFailureException(
                    "Error when retrieving all schools", ex);
        }
    }
}
