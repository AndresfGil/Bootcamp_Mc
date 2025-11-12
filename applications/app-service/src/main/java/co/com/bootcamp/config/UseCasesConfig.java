package co.com.bootcamp.config;

import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadGateway;
import co.com.bootcamp.model.bootcamp.gateways.TecnologiaGateway;
import co.com.bootcamp.usecase.bootcamp.saga.BootcampDeletionSagaOrchestrator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.bootcamp.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$|^.+ValidatorService$|^.+EnrichmentService$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    public BootcampDeletionSagaOrchestrator bootcampDeletionSagaOrchestrator(
            BootcampRepository bootcampRepository,
            CapacidadGateway capacidadGateway,
            TecnologiaGateway tecnologiaGateway
    ) {
        return new BootcampDeletionSagaOrchestrator(bootcampRepository, capacidadGateway, tecnologiaGateway);
    }
}
