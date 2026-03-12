package com.bootcamp.persona.domain.usecase;

import com.bootcamp.persona.domain.exception.EnrollmentException;
import com.bootcamp.persona.domain.exception.PersonNotFoundException;
import com.bootcamp.persona.domain.model.BootcampEnrollment;
import com.bootcamp.persona.domain.model.Person;
import com.bootcamp.persona.domain.spi.IBootcampServicePort;
import com.bootcamp.persona.domain.spi.IPersonPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonUseCaseTest {

    @Mock private IPersonPersistencePort persistencePort;
    @Mock private IBootcampServicePort bootcampServicePort;
    @InjectMocks private PersonUseCase useCase;

    @Nested
    @DisplayName("save()")
    class SaveTests {
        @Test
        @DisplayName("Debe guardar persona válida")
        void save_Valid() {
            Person p = new Person(null, "Jhoan", "jhoan@test.com", null);
            Person saved = new Person(1L, "Jhoan", "jhoan@test.com", null);
            when(persistencePort.save(any())).thenReturn(Mono.just(saved));

            StepVerifier.create(useCase.save(p))
                    .expectNextMatches(r -> r.getId().equals(1L))
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe fallar con nombre vacío")
        void save_BlankName() {
            StepVerifier.create(useCase.save(new Person(null, "", "a@b.com", null)))
                    .expectError(IllegalArgumentException.class).verify();
        }

        @Test
        @DisplayName("Debe fallar con email vacío")
        void save_BlankEmail() {
            StepVerifier.create(useCase.save(new Person(null, "Jhoan", "", null)))
                    .expectError(IllegalArgumentException.class).verify();
        }
    }

    @Nested
    @DisplayName("enrollInBootcamps() - HU-7")
    class EnrollTests {

        @Test
        @DisplayName("Debe inscribir en bootcamps sin solapamiento")
        void enroll_Valid() {
            Person p = new Person(1L, "Jhoan", "j@t.com", null);
            BootcampEnrollment b1 = new BootcampEnrollment(10L, "Boot1", LocalDate.of(2025, 1, 1), 4);
            BootcampEnrollment b2 = new BootcampEnrollment(20L, "Boot2", LocalDate.of(2025, 6, 1), 4);

            when(persistencePort.findById(1L)).thenReturn(Mono.just(p));
            when(persistencePort.countEnrollments(1L)).thenReturn(Mono.just(0L));
            when(bootcampServicePort.findById(10L)).thenReturn(Mono.just(b1));
            when(bootcampServicePort.findById(20L)).thenReturn(Mono.just(b2));
            when(persistencePort.findBootcampIdsByPersonId(1L)).thenReturn(Flux.empty());
            when(persistencePort.enrollInBootcamp(eq(1L), anyLong())).thenReturn(Mono.empty());

            StepVerifier.create(useCase.enrollInBootcamps(1L, List.of(10L, 20L)))
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe fallar si excede máximo 5 inscripciones")
        void enroll_ExceedsMax() {
            Person p = new Person(1L, "Jhoan", "j@t.com", null);
            when(persistencePort.findById(1L)).thenReturn(Mono.just(p));
            when(persistencePort.countEnrollments(1L)).thenReturn(Mono.just(4L));

            StepVerifier.create(useCase.enrollInBootcamps(1L, List.of(10L, 20L)))
                    .expectErrorMatches(e -> e instanceof EnrollmentException && e.getMessage().contains("5"))
                    .verify();
        }

        @Test
        @DisplayName("Debe fallar si hay solapamiento de fechas")
        void enroll_Overlap() {
            Person p = new Person(1L, "Jhoan", "j@t.com", null);
            BootcampEnrollment existing = new BootcampEnrollment(5L, "Existing", LocalDate.of(2025, 1, 1), 8);
            BootcampEnrollment newBoot = new BootcampEnrollment(10L, "New", LocalDate.of(2025, 2, 1), 4);

            when(persistencePort.findById(1L)).thenReturn(Mono.just(p));
            when(persistencePort.countEnrollments(1L)).thenReturn(Mono.just(1L));
            when(bootcampServicePort.findById(10L)).thenReturn(Mono.just(newBoot));
            when(persistencePort.findBootcampIdsByPersonId(1L)).thenReturn(Flux.just(5L));
            when(bootcampServicePort.findById(5L)).thenReturn(Mono.just(existing));

            StepVerifier.create(useCase.enrollInBootcamps(1L, List.of(10L)))
                    .expectErrorMatches(e -> e instanceof EnrollmentException && e.getMessage().contains("coincidan"))
                    .verify();
        }

        @Test
        @DisplayName("Debe fallar si ya está inscrito")
        void enroll_AlreadyEnrolled() {
            Person p = new Person(1L, "Jhoan", "j@t.com", null);
            BootcampEnrollment existing = new BootcampEnrollment(10L, "Boot", LocalDate.of(2025, 1, 1), 4);

            when(persistencePort.findById(1L)).thenReturn(Mono.just(p));
            when(persistencePort.countEnrollments(1L)).thenReturn(Mono.just(1L));
            when(bootcampServicePort.findById(10L)).thenReturn(Mono.just(existing));
            when(persistencePort.findBootcampIdsByPersonId(1L)).thenReturn(Flux.just(10L));

            StepVerifier.create(useCase.enrollInBootcamps(1L, List.of(10L)))
                    .expectErrorMatches(e -> e instanceof EnrollmentException && e.getMessage().contains("inscrito"))
                    .verify();
        }

        @Test
        @DisplayName("Debe fallar si persona no existe")
        void enroll_PersonNotFound() {
            when(persistencePort.findById(99L)).thenReturn(Mono.empty());

            StepVerifier.create(useCase.enrollInBootcamps(99L, List.of(10L)))
                    .expectError(PersonNotFoundException.class).verify();
        }

        @Test
        @DisplayName("Debe fallar si solapamiento entre nuevos bootcamps")
        void enroll_OverlapAmongNew() {
            Person p = new Person(1L, "Jhoan", "j@t.com", null);
            BootcampEnrollment b1 = new BootcampEnrollment(10L, "B1", LocalDate.of(2025, 1, 1), 8);
            BootcampEnrollment b2 = new BootcampEnrollment(20L, "B2", LocalDate.of(2025, 2, 1), 4);

            when(persistencePort.findById(1L)).thenReturn(Mono.just(p));
            when(persistencePort.countEnrollments(1L)).thenReturn(Mono.just(0L));
            when(bootcampServicePort.findById(10L)).thenReturn(Mono.just(b1));
            when(bootcampServicePort.findById(20L)).thenReturn(Mono.just(b2));
            when(persistencePort.findBootcampIdsByPersonId(1L)).thenReturn(Flux.empty());

            StepVerifier.create(useCase.enrollInBootcamps(1L, List.of(10L, 20L)))
                    .expectErrorMatches(e -> e instanceof EnrollmentException && e.getMessage().contains("coincidan"))
                    .verify();
        }
    }

    @Test
    @DisplayName("Debe fallar si la persona es nula")
    void save_NullPerson() {
        StepVerifier.create(useCase.save(null))
                .expectErrorMatches(e ->
                        e instanceof IllegalArgumentException &&
                                e.getMessage().contains("persona"))
                .verify();
    }
}
