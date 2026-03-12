package com.bootcamp.persona.infrastructure.input.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PersonRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    public PersonRequest() {}
    public PersonRequest(String name, String email) { this.name = name; this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
