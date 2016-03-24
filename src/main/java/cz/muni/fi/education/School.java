package cz.muni.fi.education;

/**
 * Created by John on 09-Mar-16.
 */
public class School {

    private Long id;
    private String name;
    private String address;

    /**
     * Gets id of school
     *
     * @return id of school
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id of school
     *
     * @param id unique parameter of school
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets name of school
     *
     * @return name of school
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of school
     *
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        School school = (School) o;

        if (id != null ? !id.equals(school.id) : school.id != null) return false;
        if (name != null ? !name.equals(school.name) : school.name != null) return false;
        return address != null ? address.equals(school.address) : school.address == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}
