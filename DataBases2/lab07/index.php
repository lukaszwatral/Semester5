<?php

use Lukasz\Lab7\Student;
use Lukasz\Lab7\SchoolClass;
use Lukasz\Lab7\Subject;
use Lukasz\Lab7\Teacher;

require_once __DIR__ . '/bootstrap.php';
require_once __DIR__ . '/src/Student.php';
require_once __DIR__ . '/src/SchoolClass.php';
require_once __DIR__ . '/src/Subject.php';
require_once __DIR__ . '/src/Teacher.php';

$c1 = new SchoolClass("1A", 1);
$c2 = new SchoolClass("2A", 2);

$t1 = new Teacher("Ali", "Alavi");
$t2 = new Teacher("Reza", "Rezai");
$t3 = new Teacher("Mina", "Minai");
$t4 = new Teacher("Sara", "Sarai");
$c1->setTeacher($t1);
$c2->setTeacher($t2);
$t1->setSchoolClass($c1);
$t2->setSchoolClass($c2);

$s1 = new Student("Jan", "Kowalski");
$s2 = new Student("Adam", "Nowak");
$s3 = new Student("Anna", "Kowalska");
$s4 = new Student("Katarzyna", "Nowak");
$c1->getStudents()->add($s1);
$c1->getStudents()->add($s2);
$c1->getStudents()->add($s3);
$c1->getStudents()->add($s4);
$s1->setSchoolClass($c1);
$s2->setSchoolClass($c1);
$s3->setSchoolClass($c1);
$s4->setSchoolClass($c1);

$s5 = new Student("John", "Doe");
$s6 = new Student("Jane", "Doe");
$s7 = new Student("Alice", "Doe");
$s8 = new Student("Bob", "Doe");
$c2->getStudents()->add($s5);
$c2->getStudents()->add($s6);
$c2->getStudents()->add($s7);
$c2->getStudents()->add($s8);
$s5->setSchoolClass($c2);
$s6->setSchoolClass($c2);
$s7->setSchoolClass($c2);
$s8->setSchoolClass($c2);

$sub1 = new Subject("Math 1A");
$sub2 = new Subject("Physics 1A");
$sub3 = new Subject("Chemistry 1A");
$sub4 = new Subject("Math 2A");
$sub5 = new Subject("Physics 2A");
$sub6 = new Subject("Biology 2A");
$c1->getSubjects()->add($sub1);
$c1->getSubjects()->add($sub2);
$c1->getSubjects()->add($sub3);
$sub1->setSchoolClass($c1);
$sub2->setSchoolClass($c1);
$sub3->setSchoolClass($c1);
$sub1->setTeacher($t1);
$sub2->setTeacher($t2);
$sub3->setTeacher($t3);
$c2->getSubjects()->add($sub4);
$c2->getSubjects()->add($sub5);
$c2->getSubjects()->add($sub6);
$sub4->setSchoolClass($c2);
$sub5->setSchoolClass($c2);
$sub6->setSchoolClass($c2);
$sub4->setTeacher($t1);
$sub5->setTeacher($t2);
$sub6->setTeacher($t4);
$t1->getSubject()->add($sub1);
$t1->getSubject()->add($sub4);
$t2->getSubject()->add($sub2);
$t2->getSubject()->add($sub5);
$t3->getSubject()->add($sub3);
$t4->getSubject()->add($sub6);

$entityManager->persist($c1);
$entityManager->persist($c2);
$entityManager->flush();

$query1 = $entityManager->createQuery('SELECT s FROM Lukasz\Lab7\Student s WHERE s.firstName = :fName AND s.lastName = :lName');
$query1->setParameter('fName', 'Anna');
$query1->setParameter('lName', 'Kowalska');
$student = $query1->getSingleResult();

echo 'Subjects that ' . $student->getFirstName() . ' ' . $student->getLastName() . ' attends: ' . PHP_EOL;
foreach ($student->getSchoolClass()->getSubjects() as $subject) {
    echo '- ' . $subject->getSubjectName() . ', teacher: ' . $subject->getTeacher()->getFirstName() . ' ' . $subject->getTeacher()->getLastName() . PHP_EOL;
}

$repo = $entityManager->getRepository("Lukasz\Lab7\Teacher");
$teachers = $repo->findAll();

$teachers = $repo->findBy(array('firstName' => 'Ali', 'lastName' => 'Alavi'));

echo 'Students taught by ' . $teachers[0]->getFirstName() . ' ' . $teachers[0]->getLastName() . ': ' . PHP_EOL;
foreach ($teachers[0]->getSubject() as $subject) {
    foreach ($subject->getSchoolClass()->getStudents() as $student) {
        echo '- ' . $student->getFirstName() . ' ' . $student->getLastName() . PHP_EOL;
    }
}
