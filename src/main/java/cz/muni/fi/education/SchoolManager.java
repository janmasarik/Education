package cz.muni.fi.education;

import java.util.List;

/**
 * Created by John on 09-Mar-16.
 */
public interface SchoolManager {

    /**
     * Method create new instance of school
     *
     * @param school
     */
    void createSchool(School school);

    /**
     * Method update attributes of school
     *
     * @param school
     */
    void updateSchool(School school);

    /**
     *
     * @param school
     */
    void deleteSchool(School school);

    /**
     *
     * @param id
     * @return
     */
    School findSchoolById(Long id);

    /**
     *
     *
     * @return
     */
    List<School> findAllSchools();
}
