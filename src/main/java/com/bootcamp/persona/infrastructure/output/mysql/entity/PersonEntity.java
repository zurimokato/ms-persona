package com.bootcamp.persona.infrastructure.output.mysql.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("persona")
public class PersonEntity {
    @Id
    private Long id;
    @Column("nombre")
    private String name;
    @Column("email")
    private String email;

    public PersonEntity() {}
    public PersonEntity(Long id, String name, String email) { this.id = id; this.name = name; this.email = email; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
