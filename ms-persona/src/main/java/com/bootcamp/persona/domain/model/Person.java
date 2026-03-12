package com.bootcamp.persona.domain.model;

import java.util.List;

public class Person {

    private Long id;
    private String name;
    private String email;
    private List<BootcampEnrollment> enrollments;

    public Person() {}

    public Person(Long id, String name, String email, List<BootcampEnrollment> enrollments) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.enrollments = enrollments;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<BootcampEnrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(List<BootcampEnrollment> enrollments) { this.enrollments = enrollments; }
}
