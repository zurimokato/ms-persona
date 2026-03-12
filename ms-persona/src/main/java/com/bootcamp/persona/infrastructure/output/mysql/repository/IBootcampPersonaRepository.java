package com.bootcamp.persona.infrastructure.output.mysql.repository;

import com.bootcamp.persona.infrastructure.output.mysql.entity.BootcampPersonaEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBootcampPersonaRepository extends ReactiveCrudRepository<BootcampPersonaEntity, Long> {
    Flux<BootcampPersonaEntity> findByPersonId(Long personId);
    Mono<Boolean> existsByPersonIdAndBootcampId(Long personId, Long bootcampId);
    Mono<Long> countByPersonId(Long personId);
}
