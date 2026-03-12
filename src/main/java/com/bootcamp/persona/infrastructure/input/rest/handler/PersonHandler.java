package com.bootcamp.persona.infrastructure.input.rest.handler;

import com.bootcamp.persona.domain.api.IPersonUseCasePort;
import com.bootcamp.persona.infrastructure.input.rest.dto.EnrollmentRequest;
import com.bootcamp.persona.infrastructure.input.rest.dto.PersonRequest;
import com.bootcamp.persona.infrastructure.input.rest.mapper.IPersonRequestMapper;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PersonHandler {

    private final IPersonUseCasePort personUseCasePort;
    private final IPersonRequestMapper mapper;
    private final Validator validator;

    public PersonHandler(IPersonUseCasePort personUseCasePort, IPersonRequestMapper mapper, Validator validator) {
        this.personUseCasePort = personUseCasePort;
        this.mapper = mapper;
        this.validator = validator;
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(PersonRequest.class)
                .flatMap(req -> {
                    var violations = validator.validate(req);
                    if (!violations.isEmpty()) return Mono.error(new IllegalArgumentException(violations.iterator().next().getMessage()));
                    return Mono.just(req);
                })
                .map(mapper::toDomain)
                .flatMap(personUseCasePort::save)
                .map(mapper::toResponse)
                .flatMap(resp -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(resp));
    }

    public Mono<ServerResponse> enroll(ServerRequest request) {
        Long personId = Long.valueOf(request.pathVariable("personId"));
        return request.bodyToMono(EnrollmentRequest.class)
                .flatMap(req -> personUseCasePort.enrollInBootcamps(personId, req.getBootcampIds()))
                .then(ServerResponse.ok().bodyValue("{\"message\":\"Inscripción exitosa\"}"));
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return personUseCasePort.findById(id)
                .map(mapper::toResponse)
                .flatMap(resp -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(resp));
    }
}
