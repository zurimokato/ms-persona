package com.bootcamp.persona.infrastructure.output.mysql.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("bootcamp_persona")
public class BootcampPersonaEntity {
    @Id
    private Long id;
    @Column("id_persona")
    private Long personId;
    @Column("id_bootcamp")
    private Long bootcampId;

    public BootcampPersonaEntity() {}
    public BootcampPersonaEntity(Long personId, Long bootcampId) { this.personId = personId; this.bootcampId = bootcampId; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPersonId() { return personId; }
    public void setPersonId(Long personId) { this.personId = personId; }
    public Long getBootcampId() { return bootcampId; }
    public void setBootcampId(Long bootcampId) { this.bootcampId = bootcampId; }
}
