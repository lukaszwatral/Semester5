<?php

namespace Lukasz\Lab7;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity]
class Teacher {
    #[ORM\Id]
    #[ORM\Column(type: 'integer')]
    #[ORM\GeneratedValue]
    private int $id;

    #[ORM\Column(type: 'string')]
    private string $firstName;

    #[ORM\Column(type: 'string')]
    private string $lastName;

    #[ORM\OneToOne(targetEntity: 'SchoolClass', inversedBy: 'teacher', cascade:['all'])]
    private ?SchoolClass $schoolClass;

    #[ORM\OneToMany(targetEntity: 'Subject', mappedBy: 'teacher', cascade:['all'])]
    private $subject;

    public function __construct($fName = '', $lName = ''){
        $this->firstName = $fName;
        $this->lastName = $lName;
        $this->subject = new ArrayCollection();
    }

    //getters and setters

    public function getId(): int {
        return $this->id;
    }

    public function setId(int $id): void {
        $this->id = $id;
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

    public function getSubject() {
        return $this->subject;
    }

    public function setSubject($subject): void {
        $this->subject = $subject;
    }
}