import jakarta.persistence.*;
import jdk.jfr.Experimental;
import java.util.Random;
import java.util.List;

@Entity
class Person{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String familyName;
    private int age;

    public Person() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}


public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lab5");
        EntityManager em = emf.createEntityManager();
        String[] names = {"Ali", "Reza", "Mehdi", "Hassan", "Hossein"};
        String[] families = {"Alavi", "Rezai", "Mehdizadeh", "Hassanzadeh", "Hosseinzadeh"};
        Person p = new Person();
        Random random = new Random();

        Query del = em.createQuery("DELETE FROM Person");
        em.getTransaction().begin();
        del.executeUpdate();
        em.getTransaction().commit();

        for (int i=0; i< names.length; i++){
            p = new Person();
            p.setFirstName(names[i]);
            p.setFamilyName(families[i]);
            p.setAge(random.nextInt(50)+1);
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        }

        Query q = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> people = q.getResultList();
        for (Person person : people){
            System.out.println(person.getFirstName() + " " + person.getFamilyName() + " " + person.getAge());
        }
        System.out.println("----------------------");
        Query age = em.createQuery("UPDATE Person p SET p.age = 18 WHERE p.age < 18");
        for (Person person : people){
                if (person.getAge() < 18) {
                    person.setAge(18);
                    em.getTransaction().begin();
                    age.executeUpdate();
                    em.getTransaction().commit();
                }
        }

        Query q2 = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> people2 = q2.getResultList();
        for (Person person : people2){
            System.out.println(person.getFirstName() + " " + person.getFamilyName() + " " + person.getAge());
        }

        Query q3 = em.createQuery("SELECT COUNT(p) FROM Person p WHERE p.age > 25");
        List<Person> people3 = q3.getResultList();
        System.out.println("\nLiczba osób powyżej 25 lat: " + people3.get(0));
    }
}
