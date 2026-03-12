package com.bootcamp.persona.infrastructure.output.feign.adapter;

import com.bootcamp.persona.domain.model.BootcampEnrollment;
import com.bootcamp.persona.domain.spi.IBootcampServicePort;
import com.bootcamp.persona.infrastructure.output.feign.dto.BootcampExternalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BootcampWebClientAdapter implements IBootcampServicePort {

    private final WebClient webClient;

    public BootcampWebClientAdapter(@Value("${external.bootcamp-service.url}") String baseUrl,
                                    WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<BootcampEnrollment> findById(Long id) {
        return webClient.get()
                .uri("/api/bootcamps/{id}", id)
                .retrieve()
                .onStatus(s -> s.is4xxClientError(), r -> Mono.error(new RuntimeException("Bootcamp " + id + " no encontrado")))
                .bodyToMono(BootcampExternalResponse.class)
                .map(ext -> new BootcampEnrollment(ext.getId(), ext.getName(), ext.getLaunchDate(), ext.getDurationWeeks()));
    }
}
