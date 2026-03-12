package com.bootcamp.persona.domain.model;

import java.time.LocalDate;

public class BootcampEnrollment {

    private Long bootcampId;
    private String bootcampName;
    private LocalDate launchDate;
    private Integer durationWeeks;

    public BootcampEnrollment() {}

    public BootcampEnrollment(Long bootcampId, String bootcampName, LocalDate launchDate, Integer durationWeeks) {
        this.bootcampId = bootcampId;
        this.bootcampName = bootcampName;
        this.launchDate = launchDate;
        this.durationWeeks = durationWeeks;
    }

    public Long getBootcampId() { return bootcampId; }
    public void setBootcampId(Long bootcampId) { this.bootcampId = bootcampId; }
    public String getBootcampName() { return bootcampName; }
    public void setBootcampName(String bootcampName) { this.bootcampName = bootcampName; }
    public LocalDate getLaunchDate() { return launchDate; }
    public void setLaunchDate(LocalDate launchDate) { this.launchDate = launchDate; }
    public Integer getDurationWeeks() { return durationWeeks; }
    public void setDurationWeeks(Integer durationWeeks) { this.durationWeeks = durationWeeks; }

    public LocalDate getEndDate() {
        if (launchDate == null || durationWeeks == null) return null;
        return launchDate.plusWeeks(durationWeeks);
    }

    public boolean overlapsWith(BootcampEnrollment other) {
        if (this.launchDate == null || other.getLaunchDate() == null) return false;
        LocalDate thisEnd = this.getEndDate();
        LocalDate otherEnd = other.getEndDate();
        if (thisEnd == null || otherEnd == null) return false;
        return !this.launchDate.isAfter(otherEnd) && !other.getLaunchDate().isAfter(thisEnd);
    }
}
