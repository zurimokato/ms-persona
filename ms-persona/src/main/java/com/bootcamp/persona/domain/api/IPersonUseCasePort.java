package com.bootcamp.persona.domain.api;

import com.bootcamp.persona.domain.model.Person;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IPersonUseCasePort {

    Mono<Person> save(Person person);

    Mono<Void> enrollInBootcamps(Long personId, List<Long> bootcampIds);

    Mono<Person> findById(Long id);
}
