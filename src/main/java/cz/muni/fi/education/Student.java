package cz.muni.fi.education;

/**
 * Created by John on 09-Mar-16.
 */
public class Student {

    private Long id;
    private String name;
    private java.sql.Date start;
    private java.sql.Date end;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.sql.Date getStart() {
        return start;
    }

    public void setStart(java.util.Date start) {
        this.start = new java.sql.Date(start.getTime());
    }

    public java.sql.Date getEnd() { return end; }

    public void setEnd(java.util.Date end) {
        this.end = new java.sql.Date(end.getTime()); 
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != null ? !id.equals(student.id) : student.id != null) return false;
        if (name != null ? !name.equals(student.name) : student.name != null) return false;
        if (start != null ? !start.equals(student.start) : student.start != null) return false;
        return end != null ? end.equals(student.end) : student.end == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }
}
