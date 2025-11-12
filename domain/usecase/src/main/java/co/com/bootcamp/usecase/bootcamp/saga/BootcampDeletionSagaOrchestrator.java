package co.com.bootcamp.usecase.bootcamp.saga;

import co.com.bootcamp.model.bootcamp.exception.BootcampEliminacionFallidaException;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadGateway;
import co.com.bootcamp.model.bootcamp.gateways.TecnologiaGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class BootcampDeletionSagaOrchestrator {

    private static final Logger logger = Logger.getLogger(BootcampDeletionSagaOrchestrator.class.getName());

    private final BootcampRepository bootcampRepository;
    private final CapacidadGateway capacidadGateway;
    private final TecnologiaGateway tecnologiaGateway;

    public Mono<Void> deleteBootcampWithSaga(String bootcampId) {
        Long id = Long.parseLong(bootcampId);
        
        return bootcampRepository.obtenerBootcampPorId(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Bootcamp no encontrado con ID: " + bootcampId)))
                .flatMap(bootcamp -> {
                    List<Long> capacidadIds = bootcamp.getCapacidadesIds();
                    List<Long> tecnologiaIds = bootcamp.getTecnologiasIds();
                    
                    if ((capacidadIds == null || capacidadIds.isEmpty()) && 
                        (tecnologiaIds == null || tecnologiaIds.isEmpty())) {
                        return bootcampRepository.eliminarBootcamp(id);
                    }
                    
                    return executeSaga(id, capacidadIds, tecnologiaIds);
                });
    }

    private Mono<Void> executeSaga(Long bootcampId, List<Long> capacidadIds, List<Long> tecnologiaIds) {
        logger.log(Level.INFO, "Iniciando saga para bootcamp {0}", bootcampId);
        
        return bootcampRepository.obtenerCapacidadesReferenciadasPorOtrosBootcamps(capacidadIds, bootcampId)
                .zipWith(bootcampRepository.obtenerTecnologiasReferenciadasPorOtrosBootcamps(tecnologiaIds, bootcampId))
                .flatMap(tuple -> {
                    List<Long> capacidadesReferenciadas = tuple.getT1();
                    List<Long> tecnologiasReferenciadas = tuple.getT2();
                    
                    List<Long> capacidadesADesactivar = capacidadIds.stream()
                            .filter(id -> !capacidadesReferenciadas.contains(id))
                            .toList();
                    
                    List<Long> tecnologiasADesactivar = tecnologiaIds.stream()
                            .filter(id -> !tecnologiasReferenciadas.contains(id))
                            .toList();
                    
                    List<String> capacidadIdsString = capacidadesADesactivar.stream()
                            .map(String::valueOf)
                            .toList();
                    
                    List<String> tecnologiaIdsString = tecnologiasADesactivar.stream()
                            .map(String::valueOf)
                            .toList();
                    
                    return desactivarCapacidades(capacidadIdsString)
                            .then(desactivarTecnologias(tecnologiaIdsString))
                            .then(bootcampRepository.eliminarBootcamp(bootcampId))
                            .onErrorResume(error -> {
                                logger.log(Level.SEVERE, "Error en la saga - Iniciando rollback: {0}", error.getMessage());
                                return rollback(capacidadIdsString, tecnologiaIdsString)
                                        .then(Mono.error(new BootcampEliminacionFallidaException(
                                                "No se pudo eliminar el bootcamp. Se realizó rollback de los cambios. " +
                                                "Error: " + error.getMessage()
                                        )));
                            });
                });
    }

    private Mono<Void> desactivarCapacidades(List<String> capacidadIds) {
        if (capacidadIds == null || capacidadIds.isEmpty()) {
            return Mono.empty();
        }
        
        logger.log(Level.INFO, "Desactivando capacidades: {0}", capacidadIds);
        return capacidadGateway.desactivarCapacidades(capacidadIds);
    }

    private Mono<Void> desactivarTecnologias(List<String> tecnologiaIds) {
        if (tecnologiaIds == null || tecnologiaIds.isEmpty()) {
            return Mono.empty();
        }
        
        logger.log(Level.INFO, "Desactivando tecnologías: {0}", tecnologiaIds);
        return tecnologiaGateway.desactivarTecnologias(tecnologiaIds);
    }

    private Mono<Void> rollback(List<String> capacidadIds, List<String> tecnologiaIds) {
        logger.log(Level.INFO, "ROLLBACK: Reactivando tecnologías: {0}", tecnologiaIds);
        
        Mono<Void> reactivarTecnologias = tecnologiaIds.isEmpty()
                ? Mono.empty()
                : tecnologiaGateway.activarTecnologias(tecnologiaIds)
                        .doOnSuccess(v -> logger.info("ROLLBACK: Tecnologías reactivadas exitosamente"))
                        .onErrorResume(error -> {
                            logger.log(Level.SEVERE, "ROLLBACK: Error al reactivar tecnologías: {0}", error.getMessage());
                            return Mono.empty();
                        });
        
        Mono<Void> reactivarCapacidades = capacidadIds.isEmpty() 
                ? Mono.empty()
                : capacidadGateway.activarCapacidades(capacidadIds)
                        .doOnSuccess(v -> logger.log(Level.INFO, "ROLLBACK: Capacidades reactivadas exitosamente: {0}", capacidadIds))
                        .onErrorResume(error -> {
                            logger.log(Level.SEVERE, "ROLLBACK: Error al reactivar capacidades: {0}", error.getMessage());
                            return Mono.empty();
                        });
        
        return reactivarTecnologias
                .then(reactivarCapacidades)
                .doOnSuccess(v -> logger.info("ROLLBACK: Completado exitosamente"));
    }
}

