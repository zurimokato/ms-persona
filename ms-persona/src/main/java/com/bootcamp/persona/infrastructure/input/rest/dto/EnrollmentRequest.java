package com.bootcamp.persona.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class EnrollmentRequest {
    @NotEmpty(message = "Los ids de bootcamps son obligatorios")
    private List<Long> bootcampIds;

    public EnrollmentRequest() {}
    public EnrollmentRequest(List<Long> bootcampIds) { this.bootcampIds = bootcampIds; }
    public List<Long> getBootcampIds() { return bootcampIds; }
    public void setBootcampIds(List<Long> bootcampIds) { this.bootcampIds = bootcampIds; }
}
