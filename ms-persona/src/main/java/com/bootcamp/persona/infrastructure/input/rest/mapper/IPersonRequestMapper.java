package com.bootcamp.persona.infrastructure.input.rest.mapper;

import com.bootcamp.persona.domain.model.Person;
import com.bootcamp.persona.infrastructure.input.rest.dto.PersonRequest;
import com.bootcamp.persona.infrastructure.input.rest.dto.PersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPersonRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    Person toDomain(PersonRequest request);
    PersonResponse toResponse(Person person);
}
