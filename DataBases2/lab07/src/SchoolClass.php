<?php

namespace Lukasz\Lab7;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity]
class SchoolClass {
    #[ORM\Id]
    #[ORM\Column(type: 'integer')]
    #[ORM\GeneratedValue]
    private int $id;

    #[ORM\Column(type: 'string')]
    private string $name;

    #[ORM\COLUMN(type: 'integer')]
    private int $semester;

    #[ORM\OneToMany(targetEntity: 'Student', mappedBy: 'schoolClass', cascade: ['all'])]
    private $students;

    #[ORM\OneToOne(targetEntity: 'Teacher', mappedBy: 'schoolClass', cascade: ['all'])]
    private ?Teacher $teacher;

    #[ORM\OneToMany(targetEntity: 'Subject', mappedBy: 'schoolClass', cascade: ['all'])]
    private $subjects;

    public function __construct($name = '', $sem = '') {
        $this->name = $name;
        $this->semester = $sem;
        $this->students = new ArrayCollection();
        $this->subjects = new ArrayCollection();
    }

    // getters and setters

    public function getId(): int {
        return $this->id;
    }

    public function setId(int $id): void {
        $this->id = $id;
    }

    public function getName(): string {
        return $this->name;
    }

    public function setName(string $name): void {
        $this->name = $name;
    }

    public function getSemester(): int {
        return $this->semester;
    }

    public function setSemester(int $semester): void {
        $this->semester = $semester;
    }

    public function getStudents() {
        return $this->students;
    }

    public function setStudents($students): void {
        $this->students = $students;
    }

    public function getTeacher(): ?Teacher {
        return $this->teacher;
    }

    public function setTeacher(?Teacher $teacher): void {
        $this->teacher = $teacher;
    }

    public function getSubjects() {
        return $this->subjects;
    }

    public function setSubjects($subjects): void {
        $this->subjects = $subjects;
    }
}