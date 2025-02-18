import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Subject implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long subjectID;
    private String subjectName;
    @ManyToOne(cascade = CascadeType.ALL)
    private SchoolClass schoolClass;
    @ManyToOne(cascade = CascadeType.ALL)
    Teacher teacher;

    public Subject() {
    }

    public Subject(String subjectName) {
        this.subjectName = subjectName;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public long getId() {
        return subjectID;
    }

    public void setId(long id) {
        this.subjectID = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

}

