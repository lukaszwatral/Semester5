import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
public class SchoolClass implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long classID;
    private String className;
    private int semester;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Student> students;
    @OneToOne(cascade = CascadeType.ALL)
    private Teacher teacher;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Subject> subjects;

    public SchoolClass() {
    }

    public SchoolClass(String className, int semester) {
        this.className = className;
        this.semester = semester;
        this.students = new ArrayList<>();
        this.subjects = new ArrayList<>();
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public long getId() {
        return classID;
    }

    public void setId(long id) {
        this.classID = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}

