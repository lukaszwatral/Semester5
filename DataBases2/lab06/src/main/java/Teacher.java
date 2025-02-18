import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Teacher implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long teacherID;
    private String firstName;
    private String lastName;
    @OneToOne(cascade = CascadeType.ALL)
    SchoolClass schoolClass;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Subject> subjects;

    public Teacher() {
    }

    public Teacher(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.subjects = new ArrayList<>();
    }

    public SchoolClass getTutorClass() {
        return schoolClass;
    }


    public void setTutorClass(SchoolClass tutorClass) {
        this.schoolClass = tutorClass;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public long getId() {
        return teacherID;
    }

    public void setId(long id) {
        this.teacherID = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

