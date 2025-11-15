package co.com.bootcamp.usecase.bootcamp.inscripcion;

import co.com.bootcamp.model.bootcamp.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.exception.BootcampNoEncontrado;
import co.com.bootcamp.model.bootcamp.exception.InscripcionInvalidaException;
import co.com.bootcamp.model.bootcamp.exception.UsuarioNoEncontradoException;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.gateways.InscripcionRepository;
import co.com.bootcamp.model.bootcamp.gateways.PersonaGateway;
import co.com.bootcamp.model.bootcamp.inscripcion.Inscripcion;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RequiredArgsConstructor
public class InscripcionUseCase {

    private static final int MAX_INSCRIPCIONES_ACTIVAS = 5;

    private final InscripcionRepository inscripcionRepository;
    private final PersonaGateway personaGateway;
    private final BootcampRepository bootcampRepository;

    public Mono<Inscripcion> registrarInscripcion(Inscripcion inscripcion) {
        return personaGateway.obtenerPersonaPorId(inscripcion.getPersonaId())
                .switchIfEmpty(Mono.error(new UsuarioNoEncontradoException(
                        "La persona con ID " + inscripcion.getPersonaId() + " no fue encontrada")))
                .then(bootcampRepository.obtenerBootcampPorId(inscripcion.getBootcampId())
                        .switchIfEmpty(Mono.error(new BootcampNoEncontrado(
                                "El bootcamp con ID " + inscripcion.getBootcampId() + " no fue encontrado"))))
                .flatMap(bootcampNuevo -> validarLimiteInscripciones(inscripcion.getPersonaId())
                        .then(validarSolapamientoFechas(inscripcion.getPersonaId(), bootcampNuevo))
                        .then(inscripcionRepository.registrarInscripcion(inscripcion)));
    }

    private Mono<Void> validarLimiteInscripciones(Long personaId) {
        return inscripcionRepository.contarInscripcionesActivasPorPersona(personaId)
                .flatMap(count -> {
                    if (count >= MAX_INSCRIPCIONES_ACTIVAS) {
                        return Mono.error(new InscripcionInvalidaException(
                                "No se puede inscribir en más de " + MAX_INSCRIPCIONES_ACTIVAS + " bootcamps simultáneamente. Actualmente tiene " + count + " inscripciones activas."));
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> validarSolapamientoFechas(Long personaId, Bootcamp bootcampNuevo) {
        LocalDate fechaInicioNuevo = bootcampNuevo.getFechaLanzamiento();
        LocalDate fechaFinNuevo = fechaInicioNuevo.plusWeeks(bootcampNuevo.getDuracion());

        return inscripcionRepository.obtenerBootcampIdsDeInscripcionesActivas(personaId)
                .collectList()
                .flatMap(bootcampIds -> {
                    if (bootcampIds.isEmpty()) {
                        return Mono.empty();
                    }

                    return Flux.fromIterable(bootcampIds)
                            .flatMap(bootcampRepository::obtenerBootcampPorId)
                            .collectList()
                            .flatMap(bootcampsActivos -> {
                                boolean haySolapamiento = bootcampsActivos.stream()
                                        .anyMatch(bootcamp -> {
                                            LocalDate fechaInicio = bootcamp.getFechaLanzamiento();
                                            LocalDate fechaFin = fechaInicio.plusWeeks(bootcamp.getDuracion());

                                            return seSolapan(fechaInicioNuevo, fechaFinNuevo, fechaInicio, fechaFin);
                                        });

                                if (haySolapamiento) {
                                    return Mono.error(new InscripcionInvalidaException(
                                            "No se puede inscribir en este bootcamp porque se solapa con otro bootcamp activo. El bootcamp inicia el " + fechaInicioNuevo + " y tiene una duración de " + bootcampNuevo.getDuracion() + " semanas."));
                                }

                                return Mono.empty();
                            });
                });
    }

    private boolean seSolapan(LocalDate inicio1, LocalDate fin1, LocalDate inicio2, LocalDate fin2) {
        return inicio1.isBefore(fin2) && fin1.isAfter(inicio2);
    }

}
