package com.bootcamp.persona.domain.usecase;

import com.bootcamp.persona.domain.api.IPersonUseCasePort;
import com.bootcamp.persona.domain.exception.DomainConstants;
import com.bootcamp.persona.domain.exception.EnrollmentException;
import com.bootcamp.persona.domain.exception.PersonNotFoundException;
import com.bootcamp.persona.domain.model.BootcampEnrollment;
import com.bootcamp.persona.domain.model.Person;
import com.bootcamp.persona.domain.spi.IBootcampServicePort;
import com.bootcamp.persona.domain.spi.IPersonPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class PersonUseCase implements IPersonUseCasePort {

    private final IPersonPersistencePort personPersistencePort;
    private final IBootcampServicePort bootcampServicePort;

    public PersonUseCase(IPersonPersistencePort personPersistencePort,
                         IBootcampServicePort bootcampServicePort) {
        this.personPersistencePort = personPersistencePort;
        this.bootcampServicePort = bootcampServicePort;
    }

    @Override
    public Mono<Person> save(Person person) {
        return validatePerson(person)
                .then(Mono.defer(() -> personPersistencePort.save(person)));
    }
    @Override
    public Mono<Void> enrollInBootcamps(Long personId, List<Long> bootcampIds) {
        if (bootcampIds == null || bootcampIds.isEmpty()) {
            return Mono.error(new IllegalArgumentException(DomainConstants.BOOTCAMP_IDS_REQUIRED));
        }

        return personPersistencePort.findById(personId)
                .switchIfEmpty(Mono.error(new PersonNotFoundException(personId)))
                .flatMap(person ->
                    // Check current enrollment count + new ones won't exceed 5
                    personPersistencePort.countEnrollments(personId)
                        .flatMap(currentCount -> {
                            if (currentCount + bootcampIds.size() > DomainConstants.MAX_ENROLLMENTS) {
                                return Mono.error(new EnrollmentException(DomainConstants.MAX_ENROLLMENTS_MSG));
                            }
                            // Resolve new bootcamps
                            return Flux.fromIterable(bootcampIds)
                                    .flatMap(bootcampServicePort::findById)
                                    .collectList()
                                    .flatMap(newBootcamps ->
                                        // Get existing enrollments to check overlap
                                        personPersistencePort.findBootcampIdsByPersonId(personId)
                                            .flatMap(bootcampServicePort::findById)
                                            .collectList()
                                            .flatMap(existingBootcamps -> {
                                                // Check for duplicate enrollment
                                                for (BootcampEnrollment nb : newBootcamps) {
                                                    for (BootcampEnrollment eb : existingBootcamps) {
                                                        if (nb.getBootcampId().equals(eb.getBootcampId())) {
                                                            return Mono.error(new EnrollmentException(DomainConstants.ALREADY_ENROLLED_MSG));
                                                        }
                                                    }
                                                }
                                                // Check date overlap with existing
                                                for (BootcampEnrollment nb : newBootcamps) {
                                                    for (BootcampEnrollment eb : existingBootcamps) {
                                                        if (nb.overlapsWith(eb)) {
                                                            return Mono.error(new EnrollmentException(DomainConstants.OVERLAP_MSG));
                                                        }
                                                    }
                                                }
                                                // Check overlap among new bootcamps
                                                for (int i = 0; i < newBootcamps.size(); i++) {
                                                    for (int j = i + 1; j < newBootcamps.size(); j++) {
                                                        if (newBootcamps.get(i).overlapsWith(newBootcamps.get(j))) {
                                                            return Mono.error(new EnrollmentException(DomainConstants.OVERLAP_MSG));
                                                        }
                                                    }
                                                }
                                                // All validations passed, enroll
                                                return Flux.fromIterable(bootcampIds)
                                                        .flatMap(bId -> personPersistencePort.enrollInBootcamp(personId, bId))
                                                        .then();
                                            })
                                    );
                        })
                );
    }

    @Override
    public Mono<Person> findById(Long id) {
        return personPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(new PersonNotFoundException(id)));
    }

    private Mono<Void> validatePerson(Person person) {
        if (person == null) {
            return Mono.error(new IllegalArgumentException(DomainConstants.PERSON_REQUIRED));
        }
        if (person.getName() == null || person.getName().isBlank()) {
            return Mono.error(new IllegalArgumentException(DomainConstants.NAME_REQUIRED));
        }
        if (person.getEmail() == null || person.getEmail().isBlank()) {
            return Mono.error(new IllegalArgumentException(DomainConstants.EMAIL_REQUIRED));
        }
        return Mono.empty();
    }
}
