package com.bootcamp.persona.domain.exception;

public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(Long id) {
        super("No se encontró la persona con id: " + id);
    }
}
