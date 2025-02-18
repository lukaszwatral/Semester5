import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lab6");
        EntityManager em = emf.createEntityManager();

        SchoolClass c1 = new SchoolClass("1A", 1);
        SchoolClass c2 = new SchoolClass("2A", 2);

        Teacher t1 = new Teacher("Ali", "Alavi");
        Teacher t2 = new Teacher("Reza", "Rezai");
        Teacher t3 = new Teacher("Mehdi", "Mehdizadeh");
        Teacher t4 = new Teacher("Hassan", "Hassanzadeh");
        c1.setTeacher(t1);
        c2.setTeacher(t2);
        t1.setTutorClass(c1);
        t2.setTutorClass(c2);

        Student s1 = new Student("Adam", "Smith");
        Student s2 = new Student("John", "Doe");
        Student s3 = new Student("Jane", "Doe");
        Student s4 = new Student("Alice", "Wonderland");
        c1.getStudents().add(s1);
        c1.getStudents().add(s2);
        c1.getStudents().add(s3);
        c1.getStudents().add(s4);
        s1.setSchoolClass(c1);
        s2.setSchoolClass(c1);
        s3.setSchoolClass(c1);
        s4.setSchoolClass(c1);

        Student s5 = new Student("Bob", "Builder");
        Student s6 = new Student("Dora", "Explorer");
        Student s7 = new Student("Mickey", "Mouse");
        Student s8 = new Student("Donald", "Duck");
        c2.getStudents().add(s5);
        c2.getStudents().add(s6);
        c2.getStudents().add(s7);
        c2.getStudents().add(s8);
        s5.setSchoolClass(c2);
        s6.setSchoolClass(c2);
        s7.setSchoolClass(c2);
        s8.setSchoolClass(c2);

        Subject sub1 = new Subject("Math 1A");
        Subject sub2 = new Subject("Physics 1A");
        Subject sub3 = new Subject("Chemistry 1A");
        Subject sub4 = new Subject("Math 2A");
        Subject sub5 = new Subject("Physics 2A");
        Subject sub6 = new Subject("Biology 2A");
        c1.getSubjects().add(sub1);
        c1.getSubjects().add(sub2);
        c1.getSubjects().add(sub3);
        sub1.setSchoolClass(c1);
        sub2.setSchoolClass(c1);
        sub3.setSchoolClass(c1);
        sub1.setTeacher(t1);
        sub2.setTeacher(t2);
        sub3.setTeacher(t3);
        c2.getSubjects().add(sub4);
        c2.getSubjects().add(sub5);
        c2.getSubjects().add(sub6);
        sub4.setSchoolClass(c2);
        sub5.setSchoolClass(c2);
        sub6.setSchoolClass(c2);
        sub4.setTeacher(t1);
        sub5.setTeacher(t2);
        sub6.setTeacher(t4);
        t1.getSubjects().add(sub1);
        t1.getSubjects().add(sub4);
        t2.getSubjects().add(sub2);
        t2.getSubjects().add(sub5);
        t3.getSubjects().add(sub3);
        t4.getSubjects().add(sub6);


        em.getTransaction().begin();
        em.persist(c1);
        em.persist(c2);
        em.getTransaction().commit();

        Student student = em.createQuery("SELECT s FROM Student s WHERE s.lastName = :lastName", Student.class)
                .setParameter("lastName", "Wonderland")
                .getSingleResult();
        System.out.println("Subjects that " + student.getFirstName() + " " + student.getLastName() + " attends: ");
        for (Subject sub : student.schoolClass.getSubjects()) {
            System.out.println("- " + sub.getSubjectName() + ", teacher: " + sub.getTeacher().getFirstName() + " " + sub.getTeacher().getLastName());
        }
        System.out.println();
        Teacher teacher = em.createQuery("SELECT t FROM Teacher t WHERE t.lastName = :lastName", Teacher.class)
                .setParameter("lastName", "Alavi")
                .getSingleResult();
        System.out.println("Students taught by " + teacher.getFirstName() + " " + teacher.getLastName() + ":");
        for (Subject subj : teacher.getSubjects()) {
            for (Student stud : subj.getSchoolClass().getStudents()){
                System.out.println("- " + stud.getFirstName() + " " + stud.getLastName());
            }
        }

    }
}
