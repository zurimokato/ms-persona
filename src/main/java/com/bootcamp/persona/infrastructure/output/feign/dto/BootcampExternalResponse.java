package com.bootcamp.persona.infrastructure.output.feign.dto;

import java.time.LocalDate;

public class BootcampExternalResponse {
    private Long id;
    private String name;
    private LocalDate launchDate;
    private Integer durationWeeks;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getLaunchDate() { return launchDate; }
    public void setLaunchDate(LocalDate launchDate) { this.launchDate = launchDate; }
    public Integer getDurationWeeks() { return durationWeeks; }
    public void setDurationWeeks(Integer durationWeeks) { this.durationWeeks = durationWeeks; }
}
