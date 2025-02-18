<?php

namespace Lukasz\Lab7;

use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity]
class Student {
    #[ORM\Id]
    #[ORM\Column(type: 'integer')]
    #[ORM\GeneratedValue]
    private int $studentID;

    #[ORM\Column(type: 'string')]
    private string $firstName;

    #[ORM\Column(type: 'string')]
    private string $lastName;

    #[ORM\ManyToOne(targetEntity: 'SchoolClass', inversedBy: 'students', cascade:['all'])]
    private ?SchoolClass $schoolClass;

    public function __construct($fName = '', $lName = '') {
        $this->firstName = $fName;
        $this->lastName = $lName;
    }

    //getters and setters

    public function getStudentID(): int {
        return $this->studentID;
    }

    public function setStudentID(int $studentID): void {
        $this->studentID = $studentID;
    }

    public function getFirstName(): string {
        return $this->firstName;
    }

    public function setFirstName(string $firstName): void {
        $this->firstName = $firstName;
    }

    public function getLastName(): string {
        return $this->lastName;
    }

    public function setLastName(string $lastName): void {
        $this->lastName = $lastName;
    }

    public function getSchoolClass(): ?SchoolClass {
        return $this->schoolClass;
    }

    public function setSchoolClass(?SchoolClass $schoolClass): void {
        $this->schoolClass = $schoolClass;
    }
}