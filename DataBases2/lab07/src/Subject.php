<?php

namespace Lukasz\Lab7;

use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity]
class Subject {
    #[ORM\Id]
    #[ORM\Column(type: 'integer')]
    #[ORM\GeneratedValue]
    private int $subjectID;

    #[ORM\Column(type: 'string')]
    private string $subjectName;

    #[ORM\ManyToOne(targetEntity: 'SchoolClass', inversedBy: 'subject', cascade: ['all'])]
    private ?SchoolClass $schoolClass;

    #[ORM\ManyToOne(targetEntity: 'Teacher', inversedBy: 'subject', cascade: ['all'])]
    private ?Teacher $teacher;

    public function __construct ($name = '') {
        $this->subjectName = $name;
    }


    //getters and setters

    public function getSubjectID(): int {
        return $this->subjectID;
    }

    public function setSubjectID(int $subjectID): void {
        $this->subjectID = $subjectID;
    }

    public function getSubjectName(): string {
        return $this->subjectName;
    }

    public function setSubjectName(string $subjectName): void {
        $this->subjectName = $subjectName;
    }

    public function getSchoolClass(): ?SchoolClass {
        return $this->schoolClass;
    }

    public function setSchoolClass(?SchoolClass $schoolClass): void {
        $this->schoolClass = $schoolClass;
    }

    public function getTeacher(): ?Teacher {
        return $this->teacher;
    }

    public function setTeacher(?Teacher $teacher): void {
        $this->teacher = $teacher;
    }
}