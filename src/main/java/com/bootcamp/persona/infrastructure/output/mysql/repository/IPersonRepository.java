package com.bootcamp.persona.infrastructure.output.mysql.repository;

import com.bootcamp.persona.infrastructure.output.mysql.entity.PersonEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IPersonRepository extends ReactiveCrudRepository<PersonEntity, Long> {
    Mono<Boolean> existsByEmail(String email);
}
