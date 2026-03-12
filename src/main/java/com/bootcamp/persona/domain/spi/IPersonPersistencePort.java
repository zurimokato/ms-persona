package com.bootcamp.persona.domain.spi;

import com.bootcamp.persona.domain.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPersonPersistencePort {

    Mono<Person> save(Person person);

    Mono<Person> findById(Long id);

    Mono<Boolean> existsByEmail(String email);

    Mono<Void> enrollInBootcamp(Long personId, Long bootcampId);

    Flux<Long> findBootcampIdsByPersonId(Long personId);

    Mono<Boolean> isEnrolledInBootcamp(Long personId, Long bootcampId);

    Mono<Long> countEnrollments(Long personId);
}
