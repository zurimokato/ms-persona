package com.bootcamp.persona.infrastructure.input.rest.router;

import com.bootcamp.persona.infrastructure.input.rest.handler.PersonHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class PersonRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/persons",
                    method = org.springframework.web.bind.annotation.RequestMethod.POST,
                    beanClass = PersonHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            operationId = "savePerson",
                            summary = "Crear persona",
                            description = "Crea una persona nueva",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Persona creada"),
                                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/persons/{personId}/enroll",
                    method = org.springframework.web.bind.annotation.RequestMethod.POST,
                    beanClass = PersonHandler.class,
                    beanMethod = "enroll",
                    operation = @Operation(
                            operationId = "enrollPerson",
                            summary = "Inscribir persona en bootcamps",
                            description = "Inscribe una persona en uno o varios bootcamps",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Inscripción exitosa"),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                                    @ApiResponse(responseCode = "404", description = "Persona no encontrada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/persons/{id}",
                    method = org.springframework.web.bind.annotation.RequestMethod.GET,
                    beanClass = PersonHandler.class,
                    beanMethod = "findById",
                    operation = @Operation(
                            operationId = "findPersonById",
                            summary = "Buscar persona por id",
                            description = "Retorna una persona por su identificador",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Persona encontrada"),
                                    @ApiResponse(responseCode = "404", description = "Persona no encontrada")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> personRoutes(PersonHandler handler) {
        return RouterFunctions.
                route(POST("/api/persons").and(accept(MediaType.APPLICATION_JSON)), handler::save)
                .andRoute(POST("/api/persons/{personId}/enroll").and(accept(MediaType.APPLICATION_JSON)), handler::enroll)
                .andRoute(GET("/api/persons/{id}"), handler::findById);
    }
}
