package com.bootcamp.persona.infrastructure.configuration.bean;

import com.bootcamp.persona.domain.api.IPersonUseCasePort;
import com.bootcamp.persona.domain.spi.IBootcampServicePort;
import com.bootcamp.persona.domain.spi.IPersonPersistencePort;
import com.bootcamp.persona.domain.usecase.PersonUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public IPersonUseCasePort personUseCasePort(IPersonPersistencePort persistencePort, IBootcampServicePort bootcampServicePort) {
        return new PersonUseCase(persistencePort, bootcampServicePort);
    }
}
