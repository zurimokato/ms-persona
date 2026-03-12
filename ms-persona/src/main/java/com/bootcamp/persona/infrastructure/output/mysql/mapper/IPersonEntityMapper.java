package com.bootcamp.persona.infrastructure.output.mysql.mapper;

import com.bootcamp.persona.domain.model.Person;
import com.bootcamp.persona.infrastructure.output.mysql.entity.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPersonEntityMapper {
    @Mapping(target = "enrollments", ignore = true)
    Person toDomain(PersonEntity entity);
    PersonEntity toEntity(Person person);
}
