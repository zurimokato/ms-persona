package com.bootcamp.persona.infrastructure.output.mysql.adapter;

import com.bootcamp.persona.domain.model.Person;
import com.bootcamp.persona.domain.spi.IPersonPersistencePort;
import com.bootcamp.persona.infrastructure.output.mysql.entity.BootcampPersonaEntity;
import com.bootcamp.persona.infrastructure.output.mysql.mapper.IPersonEntityMapper;
import com.bootcamp.persona.infrastructure.output.mysql.repository.IBootcampPersonaRepository;
import com.bootcamp.persona.infrastructure.output.mysql.repository.IPersonRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonMysqlAdapter implements IPersonPersistencePort {

    private final IPersonRepository personRepository;
    private final IBootcampPersonaRepository enrollmentRepository;
    private final IPersonEntityMapper mapper;

    public PersonMysqlAdapter(IPersonRepository personRepository, IBootcampPersonaRepository enrollmentRepository, IPersonEntityMapper mapper) {
        this.personRepository = personRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Person> save(Person person) {
        return personRepository.save(mapper.toEntity(person)).map(mapper::toDomain);
    }

    @Override
    public Mono<Person> findById(Long id) {
        return personRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    @Override
    public Mono<Void> enrollInBootcamp(Long personId, Long bootcampId) {
        return enrollmentRepository.save(new BootcampPersonaEntity(personId, bootcampId)).then();
    }

    @Override
    public Flux<Long> findBootcampIdsByPersonId(Long personId) {
        return enrollmentRepository.findByPersonId(personId).map(BootcampPersonaEntity::getBootcampId);
    }

    @Override
    public Mono<Boolean> isEnrolledInBootcamp(Long personId, Long bootcampId) {
        return enrollmentRepository.existsByPersonIdAndBootcampId(personId, bootcampId);
    }

    @Override
    public Mono<Long> countEnrollments(Long personId) {
        return enrollmentRepository.countByPersonId(personId);
    }
}
