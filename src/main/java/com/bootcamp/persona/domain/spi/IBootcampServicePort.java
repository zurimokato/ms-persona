package com.bootcamp.persona.domain.spi;

import com.bootcamp.persona.domain.model.BootcampEnrollment;
import reactor.core.publisher.Mono;

public interface IBootcampServicePort {

    Mono<BootcampEnrollment> findById(Long id);
}
